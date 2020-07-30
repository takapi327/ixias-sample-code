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
