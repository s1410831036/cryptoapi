CREATE TABLE Currency (
    chartName VARCHAR(255) NOT NULL,
    chineseName VARCHAR(255),
    code VARCHAR(255) NOT NULL,
    rate VARCHAR(255),
    PRIMARY KEY (chartName, code)
);
