insert into `permission` values ("MANAGE_ROLE"), ("READ_ROLE");
insert into `role_permission` (`role_name`, `permission_name`) values
    ("ADMIN", "MANAGE_ROLE"),
    ("ADMIN", "READ_ROLE"),
    ("ADMIN", "WRITE_EVENT_FESTIVAL"),
    ("ADMIN", "WRITE_EVENT_LOCAL_EVENT");