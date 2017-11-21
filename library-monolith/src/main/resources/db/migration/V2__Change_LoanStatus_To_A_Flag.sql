ALTER TABLE ITEM
ADD available BIT DEFAULT FALSE;

UPDATE ITEM
SET available = TRUE
WHERE loan_status = 'Available';

ALTER TABLE ITEM DROP COLUMN loan_status;