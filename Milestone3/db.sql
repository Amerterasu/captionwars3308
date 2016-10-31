CREATE TABLE IF NOT EXISTS `users` (
`Id` int(1) NOT NULL auto_increment,
`username` varchar(40) NOT NULL,
`password` varchar(40) NOT NULL,
`time` varchar(40) NOT NULL,
`email` varchar(40) NOT NULL,
`imageId` int(1) NOT NULL,
`captionId` int(1) NOT NULL,
PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;
INSERT INTO `users` (`Id`, `username`, `password`, `time`, `email`, `imageId`, `captionId`) VALUES
(1, 'khoale', 'worstpassword', '10012016','test1@colorado.edu', 1, 1),
(2, 'josh', 'evenworse', '10012016', 'test2@colorado.edu', 2, 2),
(3, 'drew', 'stopitnow', '10012016', 'test3@colorado.edu', 3,3),
(4, 'eric', 'randomstuff', '10012016', 'test4@colorado.edu', 4,4),
(5, 'casey', 'goodpass', '10012016', 'test5@colorado.edu', 5, 5);


CREATE TABLE IF NOT EXISTS `images` (
`Id` int(1) NOT NULL auto_increment,
`timestamp` varchar(45) NOT NULL,
`url` varchar(45) NOT NULL,
`userId` int(1) NOT NULL,
`captionId` int(1) NOT NULL,
PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;
INSERT INTO `images` (`Id`, `timestamp`, `url`, `userId`, `captionId`) VALUES
(1, '10012016', 'test.url', 1, 1),
(2, '10012016', 'test2.url', 2, 2),
(3, '10012016', 'test3.url', 3, 3),
(4, '10012016', 'test4.url', 4, 4),
(5, '10012016', 'test5.url', 5, 5);


CREATE TABLE IF NOT EXISTS `captions` (
`Id` int(1) NOT NULL auto_increment,
`time` varchar(45) NOT NULL,
`score` int(1) NOT NULL,
`likes` int(1) NOT NULL,
`userId` int(1) NOT NULL,
`imageId` int(1) NOT NULL,
PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;
INSERT INTO `captions` (`Id`, `time`, `score`, `likes`, `userId`, `imageId`) VALUES 
(1, '10012016', 101, 101, 1, 1),
(2, '10012016', 179, 179, 2, 2),
(3, '10012016', 69, 69, 3, 3),
(4, '10012016', 280, 280, 4, 4),
(5, '10012016', 340, 340, 5, 5);
