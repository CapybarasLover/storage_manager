-- liquibase formatted sql

-- changest Petra:1787037025929-9
ALTER TABLE operation
    ADD COLUMN is_cancelled BOOLEAN;

-- changest Petra:1787037025929-10
ALTER TABLE operation
    ADD COLUMN cancels_operation_id BIGINT;