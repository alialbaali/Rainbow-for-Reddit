#1.Select the last names of unemployed end-users. 

select E.lname
from end_user E
where E.username not in (select distinct username from eu_employer);

#2.Select the count of HRRs that also have an end-user proﬁle. 

select count(*)
from hrr
where endUser_username is not null;

#3. Select the company (or companies) that listed the highest paying job’s posting. 

select j.comp_cid
from job_posting as j
where j.salary = (select max(salary) from job_posting);


#4. Select the end-user that has been working at the same company for the longest period. 

select username
from eu_employer as e
where e.beginDate = (select min(beginDate) from eu_employer);


#5. Select the ﬁrst name and last name of the HRRs that posted a job listing for company C. 

select distinct h.fname, h.lname
from hrr h, job_posting j, company c
where h.username = j.hrr_username and c.cid = j.comp_cid and c.name like "oracle";

#6. Select the number of end-users that applied to job listing J. 

select count(*)
from application
where jid = 2;

#7. Select the number of applications by end-user E. 

select count(*)
from application
where username = "robertNix";

#8. Select the username of the end-user(s) that has the maximum experience. Experience is measured by  the duration of employment and does not include current employment. 

select username
from (select sum(endDate - beginDate) as exp, username
        from employment_history
        group by username) as e
where e.exp >= all (select sum(endDate - beginDate) 
                    from employment_history 
                    group by username);

#9. Select the highest paying job listing. 

select jid
from job_posting
where salary >= all (select salary from job_posting);

#10. For each end-user, list the number of applications to job listings. Order the result set by count in descending order. 

select username, count(*)
from application
group by username
order by count(*) desc;

#11. Find the jobs those are suitable for an end user E, who is looking for a part-time job to work during 
the summer in Bodrum. 

select J.description, J.salary,C.name 
from job_posting J, company C
where J.contract_type="PT" and J.comp_cid=C.cid and C.address like "%bodrum%";

#12. Find the highest paying manager job with department size<50 for an end user E. 

select jid, description, salary, c.name
from job_posting, company c
where salary = ( select max(j.salary)
				from job_posting j, manager_job_posting m
				where j.is_man_job=1 and j.jid=m.jid and m.deptSize<50)
	and comp_cid=c.cid;
	
#13. List the open internships positions of a particular company C which allows more than 20 days.

select *
from (job_posting J natural join internshipJobPosting J1) join company C on J.comp_cid = C.cid
where C.name = "somecompanynamehere"  and J1.minnumdays>20;

