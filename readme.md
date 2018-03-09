
#DHC_DB2 maven版
## 一、数据库使用

创建表空间

```sql
DROP TABLESPACE TBS_LLM_DATA INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS;
DROP TABLESPACE TBS_LLM_TEMP INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS;

create tablespace TBS_LLM_DATA datafile 'D:\app\yunqi\oradata\LMM\TBS_LLM_DATA.dbf' size 1000 M autoextend on next 100 maxsize unlimited;
create temporary tablespace TBS_LLM_TEMP tempfile 'D:\app\yunqi\oradata\LMM\TBS_LLM_TEMP.dbf' size 1000 M autoextend on next 100 maxsize unlimited;
```

默认用户test

```sql
create user test identified by test default tablespace TBS_LLM_DATA temporary tablespace TBS_LMM_TEMP;
grant connect,resource,dba to test;

--如果要更新现有数据库时,可以先将用户rad删除掉后重新建立：
-- drop user test cascade;
```

数据库表信息

`\src\main\resources\Sql\20170819\01初始化数据库.sql`

数据库表数据信息

`\src\main\resources\Sql\20170819\01初始化数据库.sql`





## 二、数据库工具

Toad（*推荐*）或者PLSQL Developer