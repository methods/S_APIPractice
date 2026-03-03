-- ============================================================
-- Local Development Data Seed
--
-- - Inserts mirror production structure (not production data)
-- - Safe to re-run due to ON CONFLICT DO NOTHING
-- ============================================================

-- Insert sample hearing results with valid UUIDs
INSERT INTO hearing_result (id, offence_id, case_id, result_level, result_label)
VALUES
    ('a1b2c3d4-e5f6-4789-8123-456789abcdef', 'b2c3d4e5-f6a7-4890-8123-456789abcdef', 'c3d4e5f6-a7b8-4901-8123-456789abcdef', 'High', 'Guilty'),
    ('d5e6f7a8-b9c0-4d12-8e34-56789abcdef0', 'e6f7a8b9-c0d1-4e23-8f45-6789abcdef01', 'f7a8b9c0-d1e2-4f34-8056-789abcdef012', 'Medium', 'Not Guilty'),
    ('1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d', '2b3c4d5e-6f7a-4b8c-9d0e-1f2a3b4c5d6e', '3c4d5e6f-7a8b-4c9d-0e1f-2a3b4c5d6e7f', 'Low', 'Dismissed')
ON CONFLICT (id) DO NOTHING;

-- Insert sample result prompts with valid UUIDs
INSERT INTO result_prompt (label, value, hearing_result_id)
VALUES
    ('Sentence Length', '12 months', 'a1b2c3d4-e5f6-4789-8123-456789abcdef'),
    ('Fines', '£1000', 'a1b2c3d4-e5f6-4789-8123-456789abcdef'),
    ('Sentence Length', 'None', 'd5e6f7a8-b9c0-4d12-8e34-56789abcdef0'),
    ('Community Service', '80 hours', 'd5e6f7a8-b9c0-4d12-8e34-56789abcdef0'),
    ('Fines', '£500', '1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d')
ON CONFLICT (hearing_result_id, label) DO NOTHING;

-- Insert sample persons with valid UUIDs
INSERT INTO person (id, hearing_id, first_name, last_name, date_of_birth, address_1, address_2, address_3, address_4, post_code)
VALUES
    ('1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d', 'd5e6f7a8-b9c0-4d12-8e34-56789abcdef0', 'John', 'Smith', '1985-05-15 00:00:00', '123 Main St', 'Apt 4B', 'London', 'Greater London', 'SW1A 1AA'),
    ('2b3c4d5e-6f7a-4b8c-9d0e-1f2a3b4c5d6e', '1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d', 'Emily', 'Johnson', '1990-08-22 00:00:00', '456 High St', '', 'Manchester', 'Greater Manchester', 'M1 2AB'),
    ('3c4d5e6f-7a8b-4c9d-0e1f-2a3b4c5d6e7f', 'f7a8b9c0-d1e2-4f34-8056-789abcdef012', 'Michael', 'Williams', '1978-11-30 00:00:00', '789 Oak Ave', '', 'Birmingham', 'West Midlands', 'B1 3CD')
ON CONFLICT (id, hearing_id) DO NOTHING;

-- Insert sample hearings with valid UUIDs
INSERT INTO hearing (id, person_id, start_date, court_centre_name, court_code, judge_name, prosecutor_name, defence_name)
VALUES
    ('d5e6f7a8-b9c0-4d12-8e34-56789abcdef0', '1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d', '2023-10-15 09:30:00', 'Central London Court', 'CLC1', 'Judge Alice Brown', 'Prosecutor David Lee', 'Defence Sarah Wilson'),
    ('1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d', '2b3c4d5e-6f7a-4b8c-9d0e-1f2a3b4c5d6e', '2023-11-20 14:00:00', 'Manchester Crown Court', 'MCC2', 'Judge Robert Green', 'Prosecutor Lisa Patel', 'Defence James Taylor'),
    ('f7a8b9c0-d1e2-4f34-8056-789abcdef012', '3c4d5e6f-7a8b-4c9d-0e1f-2a3b4c5d6e7f', '2023-12-05 10:15:00', 'Birmingham Magistrates', 'BM3', 'Judge Carol White', 'Prosecutor Mark Davis', 'Defence Olivia Moore')
ON CONFLICT (id, person_id) DO NOTHING;