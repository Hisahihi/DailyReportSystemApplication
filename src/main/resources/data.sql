INSERT INTO daily_report_system.employees(code,name,role,password,delete_flg,created_at,updated_at)
     VALUES ("1","煌木　太郎","ADMIN","$2a$10$vY93/U2cXCfEMBESYnDJUevcjJ208sXav23S.K8elE/J6Sxr4w5jO",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.employees(code,name,role,password,delete_flg,created_at,updated_at)
     VALUES ("2","田中　太郎","GENERAL","$2a$10$HPIjRCymeRZKEIq.71TDduiEotOlb8Ai6KQUHCs4lGNYlLhcKv4Wi",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.reports(report_date,title,content,employee_code,delete_flg,created_at,updated_at)
     VALUES (CURRENT_TIMESTAMP,"煌木　太郎の記載、タイトル","煌木　太郎の記載、内容",1,0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.reports(report_date,title,content,employee_code,delete_flg,created_at,updated_at)
     VALUES (CURRENT_TIMESTAMP,"田中　太郎の記載、タイトル","田中　太郎の記載、内容",2,0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.address_books(postal_number,address,phone_number,email,delete_flg,created_at,updated_at,employee_code)
     VALUES ('0000001',"北海道札幌市中央区",'09012341234',"aaaaa@gmail.com",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,"1");
INSERT INTO daily_report_system.address_books(postal_number,address,phone_number,email,delete_flg,created_at,updated_at,employee_code)
     VALUES ('0000002',"北海道札幌市中央区",'09012341235',"bbbbb@gmail.com",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,"2");