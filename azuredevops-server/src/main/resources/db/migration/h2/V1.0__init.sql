-- H2 2.1.214;

CREATE USER IF NOT EXISTS "SA" SALT '582a7e34517160c6' HASH '6b2d59d21c92ab85fb76c10fe4a0873b2745eeeae07aa2001a8c42a5577baf72' ADMIN;
CREATE SCHEMA IF NOT EXISTS "ODMEXECUTOR";

-- PIPELINE_RUNS
CREATE MEMORY TABLE "ODMEXECUTOR"."PIPELINE_RUNS"(

    "TASK_ID" BIGINT PRIMARY KEY,
    "RUN_ID" BIGINT,

    "ORGANIZATION" VARCHAR(255),
    "PROJECT" VARCHAR(255),
    "PIPELINE_ID" VARCHAR(255),
    "PIPELINE_NAME" VARCHAR(255),

    "STATUS" VARCHAR(255),
    "RESULT" VARCHAR(255),
    "VARIABLES" VARCHAR(5000),
    "TEMPLATE_PARAMETERS" VARCHAR(5000),

    "CREATED_AT" VARCHAR(255),
    "FINISHED_AT" VARCHAR(255)

);
