# ixias-sample-code
## 環境構築

```
$git clone https://github.com/takapi327/ixias-sample-code.git
```

### MySQL

```
$ sudo mysql.server start
$ mysql -u root -p
```

```sql
CREATE DATABASE ixias_sample_code;
CREATE TABLE `user` (
  `id`         BIGINT(20)   unsigned     NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(255)              NOT NULL,
  `updated_at` timestamp                 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_at` timestamp                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 参考ブログ
https://medium.com/nextbeat-engineering/%E8%87%AA%E7%A4%BEoss-ixias-%E3%81%AE%E7%B4%B9%E4%BB%8B-ixias-persistence%E3%83%91%E3%83%83%E3%82%B1%E3%83%BC%E3%82%B8%E3%81%AE%E3%82%B5%E3%83%B3%E3%83%97%E3%83%AB%E3%82%B3%E3%83%BC%E3%83%89-f1b6965fb1d6
