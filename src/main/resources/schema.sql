/*
  Spring Boot / Database-First Best Practice

  - This file defines the database schema only (no data inserts)
  - This schema mirrors the remote / legacy database structure
  - In large secured systems (e.g. HMCTS), developers typically cannot
    access production data directly
  - We rely on an exact schema mirror to develop and test locally
  - Local development relies on an exact schema mirror to ensure JPA
    mappings, queries, and tests behave correctly
  - If the schema matches production, code that passes locally
    should behave the same when deployed

  IDEMPOTENCY:
  - IF NOT EXISTS allows safe re-runs during local startup
*/

CREATE TABLE IF NOT EXISTS hearing_result (
    id UUID NOT NULL,
    offence_id UUID,
    case_id UUID,
    result_level TEXT,
    result_label TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS result_prompt (
    label TEXT NOT NULL,
    value TEXT,
    hearing_result_id UUID NOT NULL,
    PRIMARY KEY (hearing_result_id, label)
);

CREATE TABLE IF NOT EXISTS PERSON (

    id UUID NOT NULL,
    hearing_id UUID NOT NULL,
    first_name TEXT,
    last_name TEXT,
    date_of_birth TIMESTAMP,

    address_1 TEXT,
    address_2 TEXT,
    address_3 TEXT,
    address_4 TEXT,
    post_code TEXT,

    -- Composite Key
    PRIMARY KEY (id, hearing_id)
);

CREATE TABLE IF NOT EXISTS hearing (
    id UUID NOT NULL,
    person_id UUID NOT NULL,
    start_date TIMESTAMP,
    court_centre_name TEXT,
    court_code TEXT,
    judge_name TEXT,
    prosecutor_name TEXT,
    defence_name TEXT,
    PRIMARY KEY (id, person_id)
);