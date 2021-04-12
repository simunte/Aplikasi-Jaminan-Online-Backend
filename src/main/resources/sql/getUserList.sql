CREATE DEFINER=`root`@`%` PROCEDURE `uob`.`getUserList`(params VARCHAR(255), benf VARCHAR(255))
BEGIN
	IF params='IT' THEN
		SELECT u.* from users u
		join user_role ur on u.id=ur.user_id
		join `role` r on r.id=ur.role_id where u.need_approval_or_reject = params;
	ELSEIF params = 'BANK_ADMIN_1_MAKER' OR params = 'BANK_ADMIN_1_CHECKER' THEN
		SELECT u.* from users u
		join user_role ur on u.id=ur.user_id
		join `role` r on r.id=ur.role_id where u.need_approval_or_reject = 'BANK_ADMIN_1_CHECKER'
		and r.role_create='BANK_ADMIN_1_MAKER';
	ELSEIF params = 'BANK_ADMIN_2_MAKER' or params = 'BANK_ADMIN_2_CHECKER' THEN
		SELECT u.* from users u
		join user_role ur on u.id=ur.user_id
		join `role` r on r.id=ur.role_id where u.need_approval_or_reject = 'BANK_ADMIN_2_CHECKER'
		and r.role_create='BANK_ADMIN_2_MAKER' or r.role_create='BENEFICIARY_ADMIN_1' ;
	ELSEIF params = 'BENEFICIARY_ADMIN_1' OR params='BENEFICIARY_ADMIN_2'THEN
		SELECT u.* from users u
		join user_role ur on u.id=ur.user_id
		join `role` r on r.id=ur.role_id where u.need_approval_or_reject = 'BENEFICIARY_ADMIN_2'
		and r.role_create='BENEFICIARY_ADMIN_1' and u.beneficiary=benf;
	END IF;
END