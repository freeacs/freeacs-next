package tr069;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class Properties {
  private static final Logger log = LoggerFactory.getLogger(Properties.class);

  private final String digestSecret;
  private final boolean fileAuthUsed;
  private final boolean discoveryMode;
  private final String[] discoveryBlock;
  private final String authMethod;
  private final int concurrentDownloadLimit;
  private final String publicUrl;
  private final boolean appendHwVersion;
  private final Config config;

  public Properties(Config config) {
    this.config = config;
    this.authMethod = config.getString("auth.method");
    this.fileAuthUsed = config.getBoolean("file.auth.used");
    this.publicUrl = config.getString("public.url");
    this.digestSecret = config.getString("digest.secret");
    this.discoveryMode = config.getBoolean("discovery.mode");
    this.discoveryBlock = config.getStringList("discovery.block").toArray(new String[]{});
    this.concurrentDownloadLimit = config.getInt("concurrent.download.limit");
    this.appendHwVersion = config.getBoolean("unit.type.append-hw-version");
  }

  public boolean isParameterkeyQuirk(SessionData sessionData) {
    return isQuirk("parameterkey", sessionData.getUnittypeName(), sessionData.getVersion());
  }

  public boolean isUnitDiscovery(SessionData sessionData) {
    return isQuirk("unitdiscovery", sessionData.getUnittypeName(), sessionData.getVersion());
  }

  public boolean isTerminationQuirk(SessionData sessionData) {
    return isQuirk("termination", sessionData.getUnittypeName(), sessionData.getVersion());
  }

  public boolean isPrettyPrintQuirk(SessionData sessionData) {
    return isQuirk("prettyprint", sessionData.getUnittypeName(), sessionData.getVersion());
  }

  public boolean isIgnoreVendorConfigFile(SessionData sessionData) {
    return isQuirk(
        "ignorevendorconfigfile", sessionData.getUnittypeName(), sessionData.getVersion());
  }

  public boolean isNextLevel0InGPN(SessionData sessionData) {
    return isQuirk("nextlevel0ingpn", sessionData.getUnittypeName(), sessionData.getVersion());
  }

  private boolean isQuirk(String quirkName, String unittypeName, String version) {
    if (unittypeName == null) {
      log.debug("The unittypename (null) could not be found. The quirk "
              + quirkName
              + " will return default false");
      return false;
    }
    for (String quirk : getQuirks(unittypeName, version)) {
      if (quirk.equals(quirkName)) {
        return true;
      }
    }
    return false;
  }

  private String[] getQuirks(String unittypeName, String version) {
    String quirks = null;
    String versionPath = "quirks." + unittypeName + "." + version;
    if (version != null && config.hasPath(versionPath)) {
      quirks = config.getString(versionPath);
    }
    String unittypePath = "quirks." + unittypeName;
    if (quirks == null && config.hasPath(unittypePath)) {
      quirks = config.getString(unittypePath);
    }
    if (quirks != null) {
      return quirks.split("\\s*,\\s*");
    } else {
      return new String[0];
    }
  }

  public String getDigestSecret() {
    return digestSecret;
  }

  public boolean isFileAuthUsed() {
    return fileAuthUsed;
  }

  public boolean isDiscoveryMode() {
    return discoveryMode;
  }

  public String[] getDiscoveryBlock() {
    return discoveryBlock;
  }

  public String getAuthMethod() {
    return authMethod;
  }

  public int getConcurrentDownloadLimit() {
    return concurrentDownloadLimit;
  }

  public String getPublicUrl() {
    return publicUrl;
  }

  public boolean isAppendHwVersion() {
    return appendHwVersion;
  }
}
