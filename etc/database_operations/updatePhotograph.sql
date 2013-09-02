create table `PICTURE` (`OID_PHOTOGRAPH` bigint unsigned, `OID` bigint unsigned, `PICTURE_DATA` blob, `OID_DOMAIN_META_OBJECT` bigint unsigned, `ASPECT_RATIO` text, `HEIGHT` int(11), `PICTURE_FILE_FORMAT` text, `PICTURE_SIZE` text, `OJB_CONCRETE_CLASS` varchar(255) NOT NULL DEFAULT '', `WIDTH` int(11), `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_PHOTOGRAPH)) ENGINE=InnoDB, character set utf8;
alter table `PHOTOGRAPH` add `OID_ORIGINAL` bigint unsigned;
