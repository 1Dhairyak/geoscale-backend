-- Insert the default quiz
INSERT INTO quizzes (id, title, description, difficulty, active)
VALUES (1, 'Global Geography Expert', 'Test your knowledge of world geography, capitals, and flags.', 'HARD', true)
ON CONFLICT (id) DO NOTHING;

-- Insert at least 15 questions for the quiz
INSERT INTO questions (id, quiz_id, order_index, prompt, options_json, correct_answer, points, type) VALUES
(1, 1, 1, 'What is the capital city of Australia?', '["Sydney", "Melbourne", "Canberra", "Perth"]', '2', 10, 'MULTIPLE_CHOICE'),
(2, 1, 2, 'Which of these countries is completely landlocked by South Africa?', '["Eswatini", "Lesotho", "Botswana", "Namibia"]', '1', 10, 'MULTIPLE_CHOICE'),
(3, 1, 3, 'What is the longest river in the world?', '["Amazon", "Nile", "Yangtze", "Mississippi"]', '1', 10, 'MULTIPLE_CHOICE'),
(4, 1, 4, 'Which mountain range separates Europe and Asia?', '["Alps", "Himalayas", "Ural Mountains", "Rocky Mountains"]', '2', 10, 'MULTIPLE_CHOICE'),
(5, 1, 5, 'What is the smallest country in the world by land area?', '["Monaco", "Nauru", "Tuvalu", "Vatican City"]', '3', 10, 'MULTIPLE_CHOICE'),
(6, 1, 6, 'Which desert is the largest hot desert in the world?', '["Gobi", "Kalahari", "Sahara", "Arabian"]', '2', 10, 'MULTIPLE_CHOICE'),
(7, 1, 7, 'Which of these cities is located on two continents?', '["Cairo", "Istanbul", "Moscow", "Panama City"]', '1', 10, 'MULTIPLE_CHOICE'),
(8, 1, 8, 'What is the highest mountain peak in North America?', '["Mount Elbert", "Mount Whitney", "Mount Logan", "Denali"]', '3', 10, 'MULTIPLE_CHOICE'),
(9, 1, 9, 'Which ocean is the deepest?', '["Atlantic", "Indian", "Southern", "Pacific"]', '3', 10, 'MULTIPLE_CHOICE'),
(10, 1, 10, 'What is the capital of Canada?', '["Toronto", "Vancouver", "Ottawa", "Montreal"]', '2', 10, 'MULTIPLE_CHOICE'),
(11, 1, 11, 'Which country has the most islands in the world?', '["Indonesia", "Philippines", "Sweden", "Canada"]', '2', 10, 'MULTIPLE_CHOICE'),
(12, 1, 12, 'What is the largest country in South America by area?', '["Argentina", "Peru", "Colombia", "Brazil"]', '3', 10, 'MULTIPLE_CHOICE'),
(13, 1, 13, 'Which African country has the largest population?', '["Egypt", "Ethiopia", "Nigeria", "South Africa"]', '2', 10, 'MULTIPLE_CHOICE'),
(14, 1, 14, 'The Great Barrier Reef is located off the coast of which country?', '["New Zealand", "Australia", "Fiji", "Papua New Guinea"]', '1', 10, 'MULTIPLE_CHOICE'),
(15, 1, 15, 'What is the capital of Japan?', '["Kyoto", "Osaka", "Tokyo", "Yokohama"]', '2', 10, 'MULTIPLE_CHOICE')
ON CONFLICT (id) DO NOTHING;
