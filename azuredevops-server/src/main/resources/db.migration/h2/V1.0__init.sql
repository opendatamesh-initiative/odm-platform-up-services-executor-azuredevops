-- H2 2.1.214;
CREATE USER IF NOT EXISTS "SA" SALT '582a7e34517160c6' HASH '6b2d59d21c92ab85fb76c10fe4a0873b2745eeeae07aa2001a8c42a5577baf72' ADMIN;
CREATE SCHEMA IF NOT EXISTS "ODMEXECUTOR";
CREATE SEQUENCE "ODMNOTIFICATION"."HIBERNATE_SEQUENCE" START WITH 1;

CREATE MEMORY TABLE "ODMEXECUTOR"."PIPELINE_RUNS"(
    "TASKID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,

    "RUNID" BIGINT,
    "STATUS" VARCHAR(255)
);
