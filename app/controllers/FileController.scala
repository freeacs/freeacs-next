package controllers

import java.util.Objects

import dbi.{DBIHolder, File, FileType}
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import tr069.base.BaseCache
import tr069.methods.decision.GetParameterValues.DownloadLogicTR069

@Singleton
class FileController @Inject()(cc: ControllerComponents,
                               dbiHolder: DBIHolder,
                               baseCache: BaseCache) extends AbstractController(cc) with Logging {

  def getFile(fileType: String, firmwareVersionStr: String, unitTypeNameStr: String): Action[AnyContent] = Action {
    val firmwareVersion = firmwareVersionStr.replaceAll(DownloadLogicTR069.SPACE_SEPARATOR, " ")
    val unitTypeName = unitTypeNameStr.replaceAll(DownloadLogicTR069.SPACE_SEPARATOR, " ")
    val acs = dbiHolder.dbi.getAcs
    val unitType = acs.getUnittype(unitTypeName)
    if (unitType == null) {
      logger.error("Could not find unittype " + unitTypeName + " in xAPS, hence file URL is incorrect")
      NotFound
    } else {
      val firmware = unitType.getFiles.getByVersionType(firmwareVersion, FileType.valueOf(fileType))
      if (firmware == null) {
        logger.error("Could not find " + fileType + " version " + firmwareVersion + " (in unittype " + unitTypeName + ") in xAPS")
        NotFound
      } else {
        val firmwareName = firmware.getName
        logger.debug("Firmware " + firmwareName + " exists, will now retrieve binaries for unittype-name " + unitTypeName)
        val firmwareImage = readFirmwareImage(firmware)
        if (firmwareImage == null || firmwareImage.isEmpty) {
          logger.error("No binaries found for firmware " + firmwareName + " (in unittype " + unitTypeName + ")")
          NotFound
        } else {
          Ok(firmwareImage)
            .withHeaders("Content-Type" -> "application/octet-stream",
              "Content-Length" -> firmwareImage.length.toString)
        }
      }
    }
  }

  private def readFirmwareImage(firmwareFresh: File) = {
    val firmwareCache = baseCache.getFirmware(firmwareFresh.getName, firmwareFresh.getUnittype.getName)
    var firmwareReturn: File = null
    if (firmwareCache != null && Objects.equals(firmwareFresh.getId, firmwareCache.getId)) {
      firmwareReturn = firmwareCache
    } else {
      firmwareFresh.setBytes(firmwareFresh.getContent)
      baseCache.putFirmware(firmwareFresh.getName, firmwareFresh.getUnittype.getName, firmwareFresh)
      firmwareReturn = firmwareFresh
    }
    firmwareReturn.getContent
  }
}
