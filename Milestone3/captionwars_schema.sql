CREATE TABLE IF NOT EXISTS `users` (
`id` int(1) NOT NULL auto_increment,
`username` varchar(40) NOT NULL,
`password` varchar(40) NOT NULL,
`email` TINYTEXT NOT NULL,
`imageId` int(1) NOT NULL,
`captionId` int(1) NOT NULL,
`dateJoined` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
);
INSERT INTO `users` (`id`, `username`, `password`, `email`, `imageId`, `captionId`) VALUES
(1, 'khoale', 'worstpassword','test1@colorado.edu', 1, 1),
(2, 'josh', 'evenworse', 'test2@colorado.edu', 2, 2),
(3, 'drew', 'stopitnow', 'test3@colorado.edu', 3,3),
(4, 'eric', 'randomstuff', 'test4@colorado.edu', 4,4),
(5, 'casey', 'goodpass', 'test5@colorado.edu', 5, 5);


CREATE TABLE IF NOT EXISTS `images` (
`id` int(1) NOT NULL auto_increment,
`timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
`url` varchar(45) NOT NULL,
`userId` int(1) NOT NULL,
`captionId` int(1) NOT NULL,
PRIMARY KEY (`id`)
);
INSERT INTO `images` (`id`, `url`, `userId`, `captionId`) VALUES
(1, 'test.url', 1, 1),
(2, 'test2.url', 2, 2),
(3, 'test3.url', 3, 3),
(4, 'test4.url', 4, 4),
(5, 'test5.url', 5, 5);


CREATE TABLE IF NOT EXISTS `captions` (
`id` int(1) NOT NULL auto_increment,
`timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
`score` int(1) NOT NULL,
`likes` int(1) NOT NULL,
`userId` int(1) NOT NULL,
`imageId` int(1) NOT NULL,
PRIMARY KEY (`id`)
);
INSERT INTO `captions` (`id`, `score`, `likes`, `userId`, `imageId`) VALUES 
(1, 101, 101, 1, 1),
(2, 179, 179, 2, 2),
(3, 69, 69, 3, 3),
(4, 280, 280, 4, 4),
(5, 340, 340, 5, 5);
