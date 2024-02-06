create table answer_books (id bigint not null auto_increment,
                           tenant_id bigint not null,
                           additional_marks integer not null,
                           attempted_questions integer not null,
                           correct_answers integer not null,
                           created_time datetime(6) not null,
                           incorrect_answers integer not null,
                           last_updated_time datetime(6) not null,
                           obtained_marks double precision not null,
                           partially_correct_answers integer not null,
                           remarks varchar(255), session_end_time datetime(6),
                           session_start_time datetime(6), status integer,
                           time_taken integer not null,
                           total_duration integer not null,
                           total_marks integer not null,
                           total_questions integer not null,
                           batch_id bigint,
                           course_id bigint,
                           created_by bigint,
                           exam_id bigint,
                           student_id bigint,
                           updated_by bigint,
                           primary key (id)) engine=InnoDB;

create table assignment (id bigint not null auto_increment,
                         tenant_id bigint not null,
                         assignment_description varchar(255),
                         assignment_due_date datetime(6),
                         assignment_instructions_for_students varchar(255),
                         assignment_instructions_for_teachers varchar(255), assignment_name varchar(255), assignment_objective varchar(255), assignment_published_date datetime(6), assignment_status integer, assignment_total_marks integer not null, created_time datetime(6) not null, last_updated_time datetime(6) not null, resource_id bigint, batch_id bigint, course_id bigint, created_by bigint, last_updated_by bigint, batch_schedule_id bigint, student_id bigint, primary key (id)) engine=InnoDB;

create table assignment_answer_books (id bigint not null auto_increment, tenant_id bigint not null, answer TEXT, assignment_answer_book_status integer, created_time datetime(6) not null, evaluator_remarks varchar(255), last_updated_time datetime(6) not null, obtained_marks integer, student_remarks varchar(255), resource_id bigint, assignment_id bigint, created_by bigint, student_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table assignment_answers (id bigint not null auto_increment, tenant_id bigint not null, answer TEXT, created_time datetime(6) not null, last_updated_time datetime(6) not null, answer_book_id bigint, resource_id bigint, created_by bigint, question_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table assignment_questions (id bigint not null auto_increment, created_time datetime(6) not null, last_updated_time datetime(6) not null, marks integer, question VARCHAR(1024) not null, question_sort_order integer, question_type varchar(255), sample_answer_text TEXT, sample_answer_url varchar(255), word_count integer, assignment_id bigint, resource_id bigint, primary key (id)) engine=InnoDB;

create table attendance (id bigint not null auto_increment, tenant_id bigint not null, attendance_is_deleted bit, attended bit, end_date_time datetime(6), start_date_time datetime(6), schedule_id bigint, student_id bigint, primary key (id)) engine=InnoDB;

create table batch_instructor (batch_id bigint not null, instructor_id bigint not null) engine=InnoDB;

create table batch_schedules (id bigint not null auto_increment, tenant_id bigint not null, batch_schedule_is_deleted bit, description TEXT, end_date_time datetime(6), meeting_link VARCHAR(1024), resource_file_url VARCHAR(1024), start_date_time datetime(6), title varchar(255), trainer_instructions_text TEXT, batch_id bigint, primary key (id)) engine=InnoDB;

create table batches (id bigint not null auto_increment, tenant_id bigint not null, batch_additional_info TEXT, batch_custom_js TEXT, batch_delivery_format integer, batch_discount_amount integer not null, batch_discount_type integer, batch_duration_hours integer not null, batch_end_date datetime(6), batch_enrollment_capacity integer not null, batch_fees bigint not null, batch_is_deleted bit, batch_name varchar(255), batch_seo_allow_comments bit, batch_seo_allow_ratings bit, batch_seo_meta_description varchar(255), batch_seo_meta_keywords varchar(255), batch_seo_title_tag varchar(255), batch_start_date datetime(6), batch_status integer, course_id bigint, primary key (id)) engine=InnoDB;

create table chapters (id bigint not null auto_increment, tenant_id bigint not null, chapter_description TEXT, chapter_is_deleted bit, chapter_name varchar(255), chapter_sort_order integer not null, course_id bigint, primary key (id)) engine=InnoDB;

create table course_tags (id bigint not null auto_increment, tenant_id bigint not null, tag varchar(255), course_id bigint, primary key (id)) engine=InnoDB;

create table courses (id bigint not null auto_increment, tenant_id bigint not null, course_category varchar(255), course_cover_image VARCHAR(1024), course_custom_js TEXT, course_deleted bit, course_delivery_format integer, course_description TEXT not null, course_discount_amount integer not null, course_discount_type integer, course_fees integer not null, course_is_private bit, course_meta_tag_keywords varchar(255), course_name varchar(255) not null, course_seo_allow_comments bit, course_seo_allow_ratings bit, course_seo_description varchar(255), course_seo_title_tag varchar(255), course_status integer, course_thumb_image VARCHAR(1024), owner_user_id bigint, primary key (id)) engine=InnoDB;

create table enrollment (id bigint not null auto_increment, tenant_id bigint not null, enrollment_is_deleted bit, batch_id bigint, student_id bigint, primary key (id)) engine=InnoDB;

create table examquestions_mcq (id bigint not null auto_increment, tenant_id bigint not null, answer_format integer, difficulty_level integer, marks integer, question VARCHAR(1024) not null, question_format integer, question_sort_order integer, question_type varchar(255), sample_answer_text VARCHAR(8192), sample_answer_url varchar(255), correct_answer varchar(255), number_of_correct_answers integer, number_of_options integer, option1 varchar(255), option2 varchar(255), option3 varchar(255), option4 varchar(255), option5 varchar(255), option6 varchar(255), exam_id bigint, resource_id bigint, primary key (id)) engine=InnoDB;

create table examquestions_subjective (id bigint not null auto_increment, tenant_id bigint not null, answer_format integer, difficulty_level integer, marks integer, question VARCHAR(1024) not null, question_format integer, question_sort_order integer, question_type varchar(255), sample_answer_text VARCHAR(8192), sample_answer_url varchar(255), correct_answer TEXT, word_count integer, exam_id bigint, resource_id bigint, primary key (id)) engine=InnoDB;

create table examquestions_tf (id bigint not null auto_increment, tenant_id bigint not null, answer_format integer, difficulty_level integer, marks integer, question VARCHAR(1024) not null, question_format integer, question_sort_order integer, question_type varchar(255), sample_answer_text VARCHAR(8192), sample_answer_url varchar(255), correct_answer integer, option1 varchar(255), option2 varchar(255), exam_id bigint, resource_id bigint, primary key (id)) engine=InnoDB;

create table exams (id bigint not null auto_increment, tenant_id bigint not null, block_dev_tools bit(1) default false, created_time datetime(6) not null, duration_mins varchar(255), exam_expiry_date datetime(6), exam_name varchar(255), exam_publish_date datetime(6), exam_type varchar(255), last_updated_time datetime(6) not null, passing_marks integer, randomize_questions bit(1) default false, status integer, total_marks integer, batch_id bigint, chapter_id bigint, course_id bigint, created_by bigint, lesson_id bigint, student_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table feedback_questionnaires (id bigint not null auto_increment, tenant_id bigint not null, created_time datetime(6) not null, feedback_from varchar(255), feedback_type varchar(255) not null, last_updated_time datetime(6) not null, batch_id bigint, batch_schedule_id bigint, course_id bigint, created_by bigint, student_id bigint, trainer_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table feedback_questions (id bigint not null auto_increment, tenant_id bigint not null, created_time datetime(6) not null, last_updated_time datetime(6) not null, question_text varchar(255), feedback_questionnaire_id bigint, primary key (id)) engine=InnoDB;

create table feedbacks (id bigint not null auto_increment, tenant_id bigint not null, created_time datetime(6) not null, last_updated_time datetime(6) not null, star_rating integer not null, text_remark varchar(255), created_by bigint, feedback_question_id bigint, feedback_questionnaire_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table lessons (id bigint not null auto_increment, tenant_id bigint not null, allow_download bit, audio_description TEXT, audio_is_active bit, audio_size bigint not null, audio_title varchar(255), audio_url VARCHAR(1024), downloadables_description TEXT, downloadables_is_active bit, downloadables_size bigint not null, downloadables_title varchar(255), downloadables_url VARCHAR(1024), lesson_description TEXT, lesson_is_deleted bit, lesson_name varchar(255), lesson_sort_order integer not null, pdf_description TEXT, pdf_is_active bit, pdf_size bigint not null, pdf_title varchar(255), pdf_url VARCHAR(1024), presentation_description TEXT, presentation_is_active bit, presentation_size bigint not null, presentation_title varchar(255), presentation_url VARCHAR(1024), text_content TEXT, text_description TEXT, text_is_active bit, text_title varchar(255), text_url VARCHAR(1024), video_description TEXT, video_id varchar(255), video_is_active varchar(255), video_size bigint not null, video_thumbnail_url VARCHAR(1024), video_title varchar(255), video_url VARCHAR(1024), chapter_id bigint, primary key (id)) engine=InnoDB;

create table mcq_answers (id bigint not null auto_increment, tenant_id bigint not null, answer varchar(255), created_time datetime(6) not null, evaluation_result integer, evaluator_remarks varchar(255), last_updated_time datetime(6) not null, marks_given double precision not null, student_remarks varchar(255), answer_book_id bigint, created_by bigint, question_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table questionbank_mcq (id bigint not null auto_increment, tenant_id bigint not null, answer_format integer, created_time datetime(6) not null, difficulty_level integer, last_updated_time datetime(6) not null, question VARCHAR(1024) not null, question_format integer, question_type varchar(255), sample_answer_text VARCHAR(8192), sample_answer_url varchar(255), test_addition_count integer not null, correct_answer varchar(255), number_of_correct_answers integer not null, number_of_options integer not null, option1 varchar(255), option2 varchar(255), option3 varchar(255), option4 varchar(255), option5 varchar(255), option6 varchar(255), batch_id bigint, chapter_id bigint, course_id bigint, created_by bigint, lesson_id bigint, resource_id bigint, student_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table questionbank_subjective (id bigint not null auto_increment, tenant_id bigint not null, answer_format integer, created_time datetime(6) not null, difficulty_level integer, last_updated_time datetime(6) not null, question VARCHAR(1024) not null, question_format integer, question_type varchar(255), sample_answer_text VARCHAR(8192), sample_answer_url varchar(255), test_addition_count integer not null, correct_answer TEXT, word_count integer not null, batch_id bigint, chapter_id bigint, course_id bigint, created_by bigint, lesson_id bigint, resource_id bigint, student_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table questionbank_tf (id bigint not null auto_increment, tenant_id bigint not null, answer_format integer, created_time datetime(6) not null, difficulty_level integer, last_updated_time datetime(6) not null, question VARCHAR(1024) not null, question_format integer, question_type varchar(255), sample_answer_text VARCHAR(8192), sample_answer_url varchar(255), test_addition_count integer not null, correct_answer integer, option1 varchar(255), option2 varchar(255), batch_id bigint, chapter_id bigint, course_id bigint, created_by bigint, lesson_id bigint, resource_id bigint, student_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table resource_files (id bigint not null auto_increment, tenant_id bigint not null, resource_category varchar(255), resource_description varchar(255), resource_media_duration varchar(255), resource_media_resolution varchar(255), resource_name varchar(255), resource_size_kb varchar(255), resource_tags varchar(255), resource_thumbnail varchar(255), resource_type varchar(255), resource_url varchar(255), primary key (id)) engine=InnoDB;

create table resources (id bigint not null auto_increment, tenant_id bigint not null, created_time datetime(6) not null, last_updated_time datetime(6) not null, resource_category varchar(255), resource_description varchar(255), resource_for integer, resource_format integer, resource_media_duration varchar(255), resource_media_resolution varchar(255), resource_name varchar(255), resource_size_kb varchar(255), resource_tags varchar(255), resource_thumbnail varchar(255), resource_url varchar(255), created_by bigint, last_updated_by bigint, primary key (id)) engine=InnoDB;

create table subjective_answers (id bigint not null auto_increment, tenant_id bigint not null, answer TEXT, created_time datetime(6) not null, evaluation_result integer, evaluator_remarks varchar(255), last_updated_time datetime(6) not null, marks_given double precision not null, student_remarks varchar(255), answer_book_id bigint, created_by bigint, question_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table tenants (id bigint not null auto_increment, address varchar(255), company_name varchar(255) not null, corporate_email varchar(255), country_code varchar(255) not null, currency varchar(255) not null, domain_name varchar(255) not null, logo_url varchar(255), phone_number varchar(255) not null, tag_line varchar(255), tenant_is_deleted bit, time_zone varchar(255) not null, website varchar(255), primary key (id)) engine=InnoDB;

create table truefalse_answers (id bigint not null auto_increment, tenant_id bigint not null, answer integer not null, created_time datetime(6) not null, evaluation_result integer, evaluator_remarks varchar(255), last_updated_time datetime(6) not null, marks_given double precision not null, student_remarks varchar(255), answer_book_id bigint, created_by bigint, question_id bigint, updated_by bigint, primary key (id)) engine=InnoDB;

create table users (id bigint not null auto_increment, tenant_id bigint not null, address TEXT, company_name varchar(255), country varchar(255), education varchar(255), email varchar(255) not null, email_verified bit(1) default false, employee_id varchar(255), experience integer not null, first_name varchar(255) not null, is_admin bit(1) default false not null, is_instructor bit(1) default false not null, is_student bit(1) default false not null, is_super_admin bit(1) default false, last_name varchar(255) not null, occupation varchar(255), password varchar(255) not null, password_reset_requested bit, password_reset_token varchar(255), password_reset_token_expiry datetime(6), phone_number VARCHAR(15), profile_pic_url VARCHAR(1024), user_bio TEXT, user_is_deleted bit, primary key (id)) engine=InnoDB;

create table verification (id bigint not null auto_increment, expiry_date datetime(6), token varchar(255), user_id bigint, primary key (id)) engine=InnoDB;

alter table attendance drop index dupattendance;
alter table attendance add constraint dupattendance unique (student_id, schedule_id);
alter table enrollment drop index dupenrollment;
alter table enrollment add constraint dupenrollment unique (student_id, batch_id);
alter table mcq_answers drop index uniqquestion;
alter table mcq_answers add constraint uniqquestion unique (answer_book_id, question_id);
alter table subjective_answers drop index uniqquestion;
alter table subjective_answers add constraint uniqquestion unique (answer_book_id, question_id);
alter table tenants drop index domainname;
alter table tenants add constraint domainname unique (domain_name);
alter table truefalse_answers drop index uniqquestion;
alter table truefalse_answers add constraint uniqquestion unique (answer_book_id, question_id);
alter table users drop index useremail;
alter table users add constraint useremail unique (email, tenant_id);
alter table users drop index passwordtoken;
alter table users add constraint passwordtoken unique (password_reset_token);
alter table verification drop index UK_431beosusfcjpe6xad6yhlntm;
alter table verification add constraint UK_431beosusfcjpe6xad6yhlntm unique (token);
alter table verification drop index UK_a0iaxio0f0unln4qmdryyfiqg;
alter table verification add constraint UK_a0iaxio0f0unln4qmdryyfiqg unique (user_id);
alter table answer_books add constraint FK3rm4hmtv7x6149kalo40yudjm foreign key (batch_id) references batches (id);
alter table answer_books add constraint FK4cx2pgia3a7q3ulk3bl53kumv foreign key (course_id) references courses (id);
alter table answer_books add constraint FK4hb2wdui6fce1rjvcds866ixq foreign key (created_by) references users (id);
alter table answer_books add constraint FKpk7bv82f6mgiwwnvxwxu2suoq foreign key (exam_id) references exams (id);
alter table answer_books add constraint FK8u2fc2c3gahw0tu0y57ehfeup foreign key (student_id) references users (id);
alter table answer_books add constraint FKlvnbggfdh450dk8puk3d349m6 foreign key (updated_by) references users (id);
alter table assignment add constraint FK92bkv4y28olflrgwx72gbdpl6 foreign key (resource_id) references resources (id);
alter table assignment add constraint FKrpkfc4qmh88u2md9sbjg9lvvl foreign key (batch_id) references batches (id);
alter table assignment add constraint FKjdb8i3vawy0s72p9py8xvgufu foreign key (course_id) references courses (id);
alter table assignment add constraint FKo80ahieax0sxx7uwnhojuawmj foreign key (created_by) references users (id);
alter table assignment add constraint FKs33d7nm4ibdwdo0r8pvd097v6 foreign key (last_updated_by) references users (id);
alter table assignment add constraint FKsncxpmr2w8r0q8kts2w16yr08 foreign key (batch_schedule_id) references batch_schedules (id);
alter table assignment add constraint FKnr1bkdp6hxcoqc8pg5hsmpkx7 foreign key (student_id) references users (id);
alter table assignment_answer_books add constraint FKgx5a4oj56utkgedd43hhqlyqb foreign key (resource_id) references resources (id);
alter table assignment_answer_books add constraint FKiyq74t4h3ef6hlmb44ywe4404 foreign key (assignment_id) references assignment (id);
alter table assignment_answer_books add constraint FKaptj7fs6yhxu7jamevjo1wk0k foreign key (created_by) references users (id);
alter table assignment_answer_books add constraint FKnpgg523i4cgrsuwm4gw82h5b foreign key (student_id) references users (id);
alter table assignment_answer_books add constraint FK59umimb4878xcjsoc57db408r foreign key (updated_by) references users (id);
alter table assignment_answers add constraint FK9gi7d6utkwktt7l76w2ol1wl0 foreign key (answer_book_id) references assignment_answer_books (id);
alter table assignment_answers add constraint FK6api2c9kel6y6h4000b17e7w6 foreign key (resource_id) references resources (id);
alter table assignment_answers add constraint FKlx7t440p8ajmhp7g3s2ekrwmj foreign key (created_by) references users (id);
alter table assignment_answers add constraint FKg4b45n358l4ad898rf6hbiucs foreign key (question_id) references assignment_questions (id);
alter table assignment_answers add constraint FKa4yifk04ime9tt9tr6symbj3i foreign key (updated_by) references users (id);
alter table assignment_questions add constraint FKteqwklggcu23g0l99m7rqoxxi foreign key (assignment_id) references assignment (id);
alter table assignment_questions add constraint FK6aru1hb9id51ocbxoqq542hmp foreign key (resource_id) references resources (id);
alter table attendance add constraint FKlcfqxmpjye7v9yo1l22ruyorf foreign key (schedule_id) references batch_schedules (id);
alter table attendance add constraint FK80qpvlsg0xpmw80bnk64avvou foreign key (student_id) references users (id);
alter table batch_instructor add constraint FKkwp0ofjki1me8sn17sl11gxq3 foreign key (instructor_id) references users (id);
alter table batch_instructor add constraint FK3cwhpgy6mtbn2w3har4km8665 foreign key (batch_id) references batches (id);
alter table batch_schedules add constraint FKgqc1l1hxdycm54n5aqv7xr3rb foreign key (batch_id) references batches (id);
alter table batches add constraint FKfw7md94a64xgsv0otrhnbxh98 foreign key (course_id) references courses (id);
alter table chapters add constraint FK6h1m0nrtdwj37570c0sp2tdcs foreign key (course_id) references courses (id);
alter table course_tags add constraint FKjqwlxw962j7q9wdogwnrctc2p foreign key (course_id) references courses (id);
alter table courses add constraint FKba829tn8b6uk6ws8obogv5fx3 foreign key (owner_user_id) references users (id);
alter table enrollment add constraint FK3fiqt6gmichytwxs16flqe15r foreign key (batch_id) references batches (id);
alter table enrollment add constraint FKl16dtl7cgm3p2kfip5pml5jsh foreign key (student_id) references users (id);
alter table examquestions_mcq add constraint FKrl0xywqdjr8q4l2dbcnvcl4a9 foreign key (exam_id) references exams (id);
alter table examquestions_mcq add constraint FKmw1klex15fd0fdy20vlb3jb5j foreign key (resource_id) references resource_files (id);
alter table examquestions_subjective add constraint FKh195a2jr3r1r82bo1l12fgbnn foreign key (exam_id) references exams (id);
alter table examquestions_subjective add constraint FKt6xgfcf84hye2fnnl507nq5pt foreign key (resource_id) references resource_files (id);
alter table examquestions_tf add constraint FKt4mskoii4uqvatk0kxigukt5c foreign key (exam_id) references exams (id);
alter table examquestions_tf add constraint FK5949a83sqqtad0snig1jtdm5i foreign key (resource_id) references resource_files (id);
alter table exams add constraint FKq209f5605e0y2p65a4bhlj2pu foreign key (batch_id) references batches (id);
alter table exams add constraint FKfpx4i96ot47sh5lcp9hca2hlq foreign key (chapter_id) references chapters (id);
alter table exams add constraint FKr1qm93flajdaclug2fg8i7bcg foreign key (course_id) references courses (id);
alter table exams add constraint FKa9pp7fvh0i6302peis1x76ots foreign key (created_by) references users (id);
alter table exams add constraint FKl8cqdyqs24nfu8rj7elugh6nc foreign key (lesson_id) references lessons (id);
alter table exams add constraint FK48k22jeam5ak807syyopcbhth foreign key (student_id) references users (id);
alter table exams add constraint FKl7a1ul70ycu6yqj0p3077d1qn foreign key (updated_by) references users (id);
alter table feedback_questionnaires add constraint FKi0eh5aafoh12djdnkrcpe1m7q foreign key (batch_id) references batches (id);
alter table feedback_questionnaires add constraint FKchl0baab4y697d1afn1ci60kq foreign key (batch_schedule_id) references batch_schedules (id);
alter table feedback_questionnaires add constraint FK9gpqh0gkx0cnurgrh1tjat1u5 foreign key (course_id) references courses (id);
alter table feedback_questionnaires add constraint FKpsrq3nk2bkp2o23dlm6uba2v4 foreign key (created_by) references users (id);
alter table feedback_questionnaires add constraint FKn0l32gbtt0we5kdgqcioo302c foreign key (student_id) references users (id);
alter table feedback_questionnaires add constraint FKj7c6lb636r5etaikgw31npti0 foreign key (trainer_id) references users (id);
alter table feedback_questionnaires add constraint FK5xkb3a9wj1ukrtjmfwmd9eovb foreign key (updated_by) references users (id);
alter table feedback_questions add constraint FKlbnab244lcr4uql870o5bo9qw foreign key (feedback_questionnaire_id) references feedback_questionnaires (id);
alter table feedbacks add constraint FKles0odmr6glac9r0jjhp3g8ji foreign key (created_by) references users (id);
alter table feedbacks add constraint FKmup3u61go6vqag0yn1aip3yeq foreign key (feedback_question_id) references feedback_questions (id);
alter table feedbacks add constraint FKpvm95mmwsv93317h7mf7nypy2 foreign key (feedback_questionnaire_id) references feedback_questionnaires (id);
alter table feedbacks add constraint FKjccf6cs0meqrxsiv2lytqycsf foreign key (updated_by) references users (id);
alter table lessons add constraint FKmb78vk1f2oljr16oj1hpo45ma foreign key (chapter_id) references chapters (id);
alter table mcq_answers add constraint FKl9ff3lv9u2y9taw141tx58ybj foreign key (answer_book_id) references answer_books (id);
alter table mcq_answers add constraint FK9jggpkuqbsdn8k17wm7nb8jgl foreign key (created_by) references users (id);
alter table mcq_answers add constraint FKjunu93dknaey8dvlliwnhbxio foreign key (question_id) references examquestions_mcq (id);
alter table mcq_answers add constraint FKbwewcpptu2nrqtxadrfghude7 foreign key (updated_by) references users (id);
alter table questionbank_mcq add constraint FKc0y7dwcpd1q3un47pmnw6pmtx foreign key (batch_id) references batches (id);
alter table questionbank_mcq add constraint FK2yk2n5kj4ptys3yyrlg1htfmw foreign key (chapter_id) references chapters (id);
alter table questionbank_mcq add constraint FKb3l642v7flsoy6dk4iexqrb8i foreign key (course_id) references courses (id);
alter table questionbank_mcq add constraint FKst0ey5def8u4ae9gxt9rm8xn5 foreign key (created_by) references users (id);
alter table questionbank_mcq add constraint FKmtilsqi4e2a2wxsql3bw5krmd foreign key (lesson_id) references lessons (id);
alter table questionbank_mcq add constraint FKb19jkvemby63tr7vaa4v6ddj5 foreign key (resource_id) references resource_files (id);
alter table questionbank_mcq add constraint FKs4qt77ofmwmvkwg3atuo0pryj foreign key (student_id) references users (id);
alter table questionbank_mcq add constraint FKtkd6wfc8oodced1hbfnr8p0q8 foreign key (updated_by) references users (id);
alter table questionbank_subjective add constraint FKkn8vk88jtoq62pvmbl3wa2w0u foreign key (batch_id) references batches (id);
alter table questionbank_subjective add constraint FK145yn8wkjm421x5xgxpi8qfrq foreign key (chapter_id) references chapters (id);
alter table questionbank_subjective add constraint FKstpprlljtbbiwv41psep234cs foreign key (course_id) references courses (id);
alter table questionbank_subjective add constraint FKmlfbpmptjjr906jai2f0ry3xt foreign key (created_by) references users (id);
alter table questionbank_subjective add constraint FKgf7f58xiwpfjg9mqr7gr4gmpt foreign key (lesson_id) references lessons (id);
alter table questionbank_subjective add constraint FKgaehf23tv5hhmce4dxmxy3war foreign key (resource_id) references resource_files (id);
alter table questionbank_subjective add constraint FK25yw9veox1w8dwe32063x9i0c foreign key (student_id) references users (id);
alter table questionbank_subjective add constraint FKg76wd4ygn4nc7sgv56plk9yjl foreign key (updated_by) references users (id);
alter table questionbank_tf add constraint FK6i9524bibnqnhqqaygkayw6yp foreign key (batch_id) references batches (id);
alter table questionbank_tf add constraint FKt6cmnsiph8j7qhy8ofdr1ga3h foreign key (chapter_id) references chapters (id);
alter table questionbank_tf add constraint FKglxb120pq5dfu5lxoeyub1awo foreign key (course_id) references courses (id);
alter table questionbank_tf add constraint FK2cy5dmoo2qtkcd398tglblaxd foreign key (created_by) references users (id);
alter table questionbank_tf add constraint FKmwob77pcp7wpmiyop6fn1hb0p foreign key (lesson_id) references lessons (id);
alter table questionbank_tf add constraint FKsgon35ia39jg1xcu4erthngs9 foreign key (resource_id) references resource_files (id);
alter table questionbank_tf add constraint FKjp81qxgwa70d2px0pw9i5ux41 foreign key (student_id) references users (id);
alter table questionbank_tf add constraint FK41dkluf4q38qut34xecuh8lar foreign key (updated_by) references users (id);
alter table resources add constraint FKlhh4no92yvmw662cmjhi6nn6j foreign key (created_by) references users (id);
alter table resources add constraint FK1u33aittmm4abn52kccq4eqxj foreign key (last_updated_by) references users (id);
alter table subjective_answers add constraint FKst64w4k9yurqu91bg1au2g21m foreign key (answer_book_id) references answer_books (id);
alter table subjective_answers add constraint FKqq6dna5ewk6tg5umckg9oi16u foreign key (created_by) references users (id);
alter table subjective_answers add constraint FKrrfcav7r0ufnrwn83ddiw7e9e foreign key (question_id) references examquestions_subjective (id);
alter table subjective_answers add constraint FKlpirvgh4yk5k5l01qsy62sakc foreign key (updated_by) references users (id);
alter table truefalse_answers add constraint FKj11nd63imhooq6w2fcy1v8ojo foreign key (answer_book_id) references answer_books (id);
alter table truefalse_answers add constraint FKntpfdk5gght9ybb9e25gtls8y foreign key (created_by) references users (id);
alter table truefalse_answers add constraint FK79ys2iuw9ti2jvtammmqe07x3 foreign key (question_id) references examquestions_tf (id);
alter table truefalse_answers add constraint FKr1i1yk5yrkyteud35x7a3in6l foreign key (updated_by) references users (id);
alter table verification add constraint FK7ntgdvdvok1jx29t3uooau08j foreign key (user_id) references users (id);