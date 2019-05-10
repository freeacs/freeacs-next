-- Testdats

-- !Ups

insert into acs.unit_type(unit_type_id, unit_type_name, matcher_id, vendor_name, description, protocol)
values(1, 'Test', null, 'Vendor', 'Description', 'TR069');

insert into acs.unit_type_param(unit_type_param_id, unit_type_id, name, flags)
values(1, 1, 'System.X_FREEACS-COM.Secret', 'X');

insert into acs.profile(profile_id, unit_type_id, profile_name)
values(1, 1, 'Default');

insert into acs.unit(unit_id, profile_id, unit_type_id)
values('test', 1, 1);

insert into acs.unit_param(unit_id, unit_type_param_id, value)
values('test', 1, 'test');

-- !Downs
