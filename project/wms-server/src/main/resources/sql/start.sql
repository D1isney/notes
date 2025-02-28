-- 对member表进行批量更新，减少扫描次数
UPDATE `member`
SET online = CASE
                 WHEN online = 1 THEN 0
                 ELSE online
    END,
    status = CASE
                 WHEN expiration_time < NOW() THEN 2
                 ELSE status
        END
WHERE 1 = 1;


UPDATE `inventory`
SET status = CASE
                 WHEN status = 1 THEN 0 -- 更新特定状态
                 WHEN status = 3 THEN 5 -- 更新另一特定状态
                 ELSE status -- 其他状态保持不变
    END
WHERE status IN (1, 3);

UPDATE `task`
SET status = 2
WHERE status = 1;