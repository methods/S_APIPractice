/*
  Spring Boot / Database-First Best Practice (Read-Side CQRS)

  - This file defines the local database schema for the Hearings Demo.
  - It mirrors the production schemas defined in the source (see: https://github
  .com/hmcts/cpp-context-results/tree/main/results-viewstore/results-viewstore-liquibase/src/main/resources/liquibase/results-view-store-db-changesets) project's Liquibase changesets.
  - Since this application follows a Read-only CQRS pattern, these tables are
    marked as @Immutable in Java and are populated by external processes.
  - Local development relies on this mirror to ensure JPA mappings and
    Criteria Queries behave correctly against the expected structure.

  IDEMPOTENCY:
  - IF NOT EXISTS allows safe re-runs during local setup or container restarts.
*/

-- 1. NCES_Email_Notification
CREATE TABLE IF NOT EXISTS nces_email_notification (
    id UUID PRIMARY KEY,
    material_id UUID,
    notification_id UUID,
    master_defendant_id UUID,
    send_to TEXT,
    subject TEXT
);

CREATE INDEX IF NOT EXISTS nces_email_notification_material_id_idx
ON nces_email_notification (material_id);


-- 2. Informant_Register
CREATE TABLE IF NOT EXISTS informant_register (
    id UUID PRIMARY KEY,
    prosecution_authority_id UUID NOT NULL,
    prosecution_authority_code TEXT,
    prosecution_authority_ou_code TEXT,
    register_date DATE NOT NULL,
    file_id UUID,
    payload TEXT NOT NULL,
    status TEXT,
    processed_on TIMESTAMP,
    hearing_id UUID,
    register_time TIMESTAMP,
    generated_time TIMESTAMP,
    generated_date DATE
);


-- 3. Hearing_Resulted_Document
CREATE TABLE IF NOT EXISTS hearing_resulted_document (
    hearing_id UUID NOT NULL,
    hearing_day DATE NOT NULL,
    start_date DATE,
    end_date DATE,
    payload TEXT,
    PRIMARY KEY (hearing_id, hearing_day)
);


-- 4. Defendant_Tracking_Status
-- Consolidating initial creation and subsequent 'changeset 25' updates
CREATE TABLE IF NOT EXISTS defendant_tracking_status (
    offence_id UUID PRIMARY KEY,
    defendant_id UUID NOT NULL,
    em_last_modified_time TIMESTAMP,
    em_status BOOLEAN DEFAULT FALSE,
    woa_status BOOLEAN DEFAULT FALSE,
    woa_last_modified_time TIMESTAMP
);


-- 5. Defendant_GOB_Accounts
CREATE TABLE IF NOT EXISTS defendant_gob_accounts (
    master_defendant_id UUID NOT NULL,
    hearing_id UUID NOT NULL,
    account_correlation_id UUID NOT NULL,
    account_number TEXT,
    account_request_time TIMESTAMP,
    case_references TEXT NOT NULL,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL,
    CONSTRAINT defendant_gob_accounts_pk PRIMARY KEY (master_defendant_id, account_correlation_id)
);

CREATE INDEX IF NOT EXISTS defendant_gob_accounts_master_defendant_id_hearing_id_idx
ON defendant_gob_accounts (master_defendant_id, hearing_id);


-- ==============================================================================
-- SEED DATA: Providing "Furniture" for your Read-Only API
-- ==============================================================================

-- Seed a GOB Account
-- ON CONFLICT DO NOTHING ensures we don't get errors if the row already exists.
INSERT INTO defendant_gob_accounts
(master_defendant_id, hearing_id, account_correlation_id, account_number, account_request_time, case_references, created_time, updated_time)
VALUES
('550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655441111', '770e8400-e29b-41d4-a716-446655442222', 'GOB-999888', CURRENT_TIMESTAMP, 'REF-J-123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (master_defendant_id, account_correlation_id) DO NOTHING;

-- Seed a Tracking Status
INSERT INTO defendant_tracking_status
(offence_id, defendant_id, em_status, woa_status)
VALUES
('111e8400-e29b-41d4-a716-446655443333', '550e8400-e29b-41d4-a716-446655440000', true, false)
ON CONFLICT (offence_id) DO NOTHING;