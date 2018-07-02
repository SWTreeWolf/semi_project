create table Reservation(
   l_num number NOT NULL,
   l_datein date NOT NULL,
   l_dateout date NOT NULL,
   p_num number NOT NULL,
   r_num number NOT NULL,
   l_tipNum number NOT NULL,
   r_total number,
   yes number default 0,
   PRIMARY KEY (l_num)
);

CREATE TABLE Room
(
   r_num number NOT NULL,
   r_name varchar2(100) NOT NULL,
   r_pay number NOT NULL,
   r_limitedNumber number NOT NULL,
   r_contents varchar2(1000),
   PRIMARY KEY (r_num)
);

CREATE TABLE Person
(
   p_name varchar2(50) NOT NULL,
   p_num number NOT NULL,
   p_phoneNumber varchar2(50) NOT NULL,
   p_email varchar2(50) NOT NULL,
   p_contents varchar2(3000),
   l_tipNum number NOT NULL, 
   p_address varchar2(100),
   p_birth varchar2(50),
   PRIMARY KEY (p_num)
);

create table noticeboard(
   	num number constraint semiboard_num_pk primary key,
   	readcount number default 0, 
   	writer varchar2(30),
	subject varchar2(50),
	summernote varchar2(1000),
	reg_date date,
	ref number,
	re_step number,
	re_level number,
	upload varchar2(300)
);

select * from Reservation
select * from person
select * from room

select * from noticeboard

/* Drop Tables */
DROP TABLE Reservation CASCADE CONSTRAINTS;
DROP TABLE Person CASCADE CONSTRAINTS;
DROP TABLE Room CASCADE CONSTRAINTS;

DROP TABLE noticeboard CASCADE CONSTRAINTS;

/* Delete colums*/
delete from reservation;

/* Create Sequence */
create sequence person_seq
start with 1
increment by 1
nocache
nocycle;

create sequence reserv_seq
start with 1
increment by 1
nocache
nocycle;

create sequence room_seq
start with 1
increment by 1
nocache
nocycle;

create sequence noticeboard_seq 
start with 1 
increment by 1
nocache
nocycle;

drop sequence person_seq;
drop sequence reserv_seq;
drop sequence room_seq;

/* Create Foreign Keys */

ALTER TABLE Reservation
   ADD FOREIGN KEY (p_num)
   REFERENCES Person (p_num)
;

ALTER TABLE Reservation
   ADD FOREIGN KEY (r_num)
   REFERENCES Room (r_num)
;

-------------------------------------------------------------------
/* insert colum */

/* reservation */
insert into Reservation values(reserv_seq.nextval,TO_DATE('2018-06-27','YYYY-MM-DD'),TO_DATE('2018-06-28','YYYY-MM-DD'),1,1,78977,320000,0);
insert into Reservation values(reserv_seq.nextval,TO_DATE('2018-06-30','YYYY-MM-DD'),TO_DATE('2018-07-02','YYYY-MM-DD'),2,1,55631,520000,1);
insert into Reservation values(reserv_seq.nextval,TO_DATE('2018-06-01','YYYY-MM-DD'),TO_DATE('2018-06-09','YYYY-MM-DD'),3,2,47534,620000,0);
insert into Reservation values(reserv_seq.nextval,TO_DATE('2018-06-05','YYYY-MM-DD'),TO_DATE('2018-06-08','YYYY-MM-DD'),4,3,78347,250000,1);

select r.*,p.p_name from reservation r, person p where r.l_tipNum=p.l_tipNum

/* room */
insert into room values(room_seq.nextval,'조명이 있는방',90000,4,'정말정말 좋은방이에요^^ 너무너무너무 ㅠㅠㅠㅠㅠㅠㅠ흐흐그흐ㅡㄱ흐그흑');
insert into room values(room_seq.nextval,'경치가 아름다운 방',120000,5,'비싸지만 좋은방입니다^^^^^6ㅇㅇㅅㅇㅅㅇㅅㅇㅅ케케케케케');
insert into room values(room_seq.nextval,'가족이 즐겨찾는 방',20000,2,'두명밖에 못 들어가는 방이에에요.... ');
insert into room values(room_seq.nextval,'아름다운 디자인의 방',200000,5,'정말 좋은방입니다 추천해요 뭐가좋냐면... ㅎㅎㅎㅎ');
insert into room values(room_seq.nextval,'바다가 보이는 방',200000,5,'정말 좋은방입니다 추천해요 뭐가좋냐면... ㅎㅎㅎㅎ');

/* person */
insert into person values('미나',person_seq.nextval,'444','ㅇㅇ','ㅇ',78977,'ㅇㅇ','ㅇㅇ');
insert into person values('츄츄',person_seq.nextval,'444','ㅇㅇ','ㅇ',55631,'ㅇㅇ','ㅇㅇ');
insert into person values('키키',person_seq.nextval,'444','ㅇㅇ','ㅇ',47534,'ㅇㅇ','ㅇㅇ');
insert into person values('미미',person_seq.nextval,'444','ㅇㅇ','ㅇ',78347,'ㅇㅇ','ㅇㅇ');

/* notice */
insert into noticeboard
values(noticeboard_seq.nextval,0,'한상빈','제목1','내용테스트',sysdate,semiboard_seq.nextval,0,0,'sample.txt');



/*6월6일의 매출*/
select sum(round(r_total/(l_dateout-l_datein))) from res where l_datein <= TO_DATE('2018-06-06','YYYY-MM-DD') and
l_dateout >= TO_DATE('2018-06-06','YYYY-MM-DD') 

select r_total,r_num from res where l_datein >= TO_DATE('2018-06-01','YYYY-MM-DD')  
l_dateout <= TO_DATE('2018-06-30','YYYY-MM-DD') 

delete from room;
delete from reservation;
delete from person;

SELECT *
FROM room 
WHERE r_num NOT IN 
(
 	SELECT RoomID 
    FROM   r.res, ro.room
    WHERE r.r_num=ro.r_num
    and (l_datein <= 입실 AND l_dateout >= 입실) 
           OR (l_datein <퇴실 AND l_dateout >= 퇴실 )
           OR (입실 <= l_datein AND 퇴실 >= l_datein)
)
/*
l_datein -> TO_DATE('2018-06-20','YYYY-MM-DD'),l_dateout -> TO_DATE('2018-06-28','YYYY-MM-DD')
2번방을 예약할 때 
*/
SELECT *
FROM room 
WHERE r_limitednumber>=1 and r_num NOT IN 
( SELECT r_num
    FROM   res r
    WHERE
   		(l_datein <= TO_DATE('2018-06-27','YYYY-MM-DD') AND l_dateout >= TO_DATE('2018-06-27','YYYY-MM-DD'))
           OR (l_datein < TO_DATE('2018-06-28','YYYY-MM-DD') AND l_dateout >= TO_DATE('2018-06-28','YYYY-MM-DD') )
           OR (TO_DATE('2018-06-27','YYYY-MM-DD') <= l_datein AND TO_DATE('2018-06-28','YYYY-MM-DD') >= l_datein)
)





drop table person
delete from reservation

drop table person



 
 
/*선택한 날짜 & 방에 예약이 되어있는지 확인*/ 
select * from reservation where l_date between TO_DATE('2018-06-27','YYYY-MM-DD') and TO_DATE('2018-06-27','YYYY-MM-DD')
and r_num=2;
 
select * from reservation where l_date=TO_DATE('2018-07-28','YYYY-MM-DD') and r_num=1
  
 
 
 
/*선택한 날짜&방에 예약이 안되어있는 방 나타내기*/
select * from room where r_num not in
( select r_num from reservation 
where l_date between TO_DATE('2018-07-28','YYYY-MM-DD') and TO_DATE('2018-07-31','YYYY-MM-DD'))
and r_limitednumber>=1
 

 
select * from reservation 
where l_date between TO_DATE('2018-06-27','YYYY-MM-DD') and TO_DATE('2018-06-30','YYYY-MM-DD')
 
 select * from reservation where l_tipNum=90772
 
select * from reservation;


select * from person

select * from reservation r, person p where r.l_tipnum=p.l_tipnum and p.p_name='최미나' 

drop table room
select * from room


