-- Create the Logging table
CREATE TABLE logging (
    req_desc TEXT NOT NULL,
    ip VARCHAR(255) NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    timestamp DATETIME NOT NULL
);

-- Create the Review table
CREATE TABLE review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    rating FLOAT NOT NULL,
    id_user INT NOT NULL,
    name_user VARCHAR(255) NOT NULL,
    profile_img VARCHAR(255) NOT NULL,
    id_restaurant INT NOT NULL
);