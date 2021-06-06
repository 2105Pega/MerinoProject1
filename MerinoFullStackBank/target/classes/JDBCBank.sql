create table account_table (
	BANK_ACCOUNT_ID serial primary key,
	ACCOUNT_TYPE varchar(20) check(ACCOUNT_TYPE in ('Checking', 'Savings')) not null,
	ACCOUNT_BALANCE double precision not null,
	ACCOUNT_APPROVED varchar(20) check(ACCOUNT_APPROVED in ('Approved', 'Pending', 'Cancelled'))  not null

);
alter table account_table add constraint possitive_balance Check(account_balance >= 0);

create table user_table (
	USER_ID serial primary key,
	USER_NAME varchar(26) unique,
	USER_PASS varchar(26) not null,
	USER_F_NAME varchar(26) not null,
	USER_L_NAME varchar(26) not null
);
create table user_type_table (
	type_id int primary key,
	type_name varchar(20) not null
);
insert into user_type_table values (1, 'Customer'), (2, 'Employee'), (3, 'Administrator');

alter table user_table add column user_type int references user_type_table not null;

ALTER TABLE user_table
ALTER COLUMN user_name SET NOT null;







create table user_account_table (
	USER_ID int references user_table(USER_ID),
	BANK_ACCOUNT_ID int references account_table(BANK_ACCOUNT_ID),
	primary key (USER_ID, BANK_ACCOUNT_ID)
);


create table personal_information_table (
	user_id int references user_table(user_id) unique,
	phone_number varchar(20),
	address varchar (92)
	
)
