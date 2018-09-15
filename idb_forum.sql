

CREATE TABLE `question` (
  `ques_id` int(10) UNSIGNED NOT NULL,
  `ques_title` varchar(200) NOT NULL,
  `ques_desc` varchar(1000) NOT NULL,
  `up_vote` int(10) UNSIGNED DEFAULT '0',
  `comment_count` int(10) UNSIGNED DEFAULT '0',
  `ques_date` datetime NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `image_url` varchar(100) DEFAULT NULL,
  `isactive` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `ques_ans` (
  `ans_id` int(10) UNSIGNED NOT NULL,
  `ques_id` int(10) UNSIGNED NOT NULL,
  `ans_desc` varchar(1000) NOT NULL,
  `up_vote` int(11) NOT NULL DEFAULT '0',
  `user_id` int(10) UNSIGNED NOT NULL,
  `ans_time` datetime NOT NULL,
  `isactive` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `ques_tag` (
  `tag_id` int(10) UNSIGNED NOT NULL,
  `tag_name` varchar(45) NOT NULL,
  `tag_color` varchar(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `ques_tag_link` (
  `link_id` int(10) UNSIGNED NOT NULL,
  `ques_id` int(10) UNSIGNED NOT NULL,
  `tag_id` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `user` (
  `user_id` int(10) UNSIGNED NOT NULL,
  `role_id` int(10) UNSIGNED NOT NULL,
  `user_name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `profile_pic` varchar(100) DEFAULT NULL,
  `isactive` int(11) NOT NULL DEFAULT '1',
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `instructor_id` int(11) NOT NULL DEFAULT '0',
  `tsp_name` varchar(150) DEFAULT NULL,
  `trainee_id` int(11) NOT NULL DEFAULT '0',
  `batch_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `user_role` (
  `role_id` int(10) UNSIGNED NOT NULL,
  `role_name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`role_id`, `role_name`) VALUES
(1, 'administrator'),
(2, 'pa'),
(3, 'pc'),
(4, 'instructors'),
(5, 'trainees');



--
-- Indexes for table `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`ques_id`);

--
-- Indexes for table `ques_ans`
--
ALTER TABLE `ques_ans`
  ADD PRIMARY KEY (`ans_id`),
  ADD KEY `FK_ques_ans_qid` (`ques_id`);

--
-- Indexes for table `ques_tag`
--
ALTER TABLE `ques_tag`
  ADD PRIMARY KEY (`tag_id`);

--
-- Indexes for table `ques_tag_link`
--
ALTER TABLE `ques_tag_link`
  ADD PRIMARY KEY (`link_id`),
  ADD KEY `FK_ques_tag_link_ques` (`ques_id`),
  ADD KEY `FK_ques_tag_link_tag` (`tag_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `FK_user_role` (`role_id`);


--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--


--
-- AUTO_INCREMENT for table `question`
--
ALTER TABLE `question`
  MODIFY `ques_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `ques_ans`
--
ALTER TABLE `ques_ans`
  MODIFY `ans_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `ques_tag`
--
ALTER TABLE `ques_tag`
  MODIFY `tag_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `ques_tag_link`
--
ALTER TABLE `ques_tag_link`
  MODIFY `link_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;


--
-- AUTO_INCREMENT for table `user_role`
--
ALTER TABLE `user_role`
  MODIFY `role_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--


--
-- Constraints for table `ques_ans`
--
ALTER TABLE `ques_ans`
  ADD CONSTRAINT `FK_ques_ans_qid` FOREIGN KEY (`ques_id`) REFERENCES `question` (`ques_id`);

--
-- Constraints for table `ques_tag_link`
--
ALTER TABLE `ques_tag_link`
  ADD CONSTRAINT `FK_ques_tag_link_ques` FOREIGN KEY (`ques_id`) REFERENCES `question` (`ques_id`),
  ADD CONSTRAINT `FK_ques_tag_link_tag` FOREIGN KEY (`tag_id`) REFERENCES `ques_tag` (`tag_id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FK_user_role` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`role_id`);
COMMIT;
