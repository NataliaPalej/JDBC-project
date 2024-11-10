/* Natalia Palej A00279259 */

/********************************************************
*     This script creates the database named makeup     * 
*********************************************************/
drop database if exists makeupdb;
create database makeupdb;
use makeupdb;

/*****************************************
*              Create Tables             * 
******************************************/
-- Products Table
create table products (
  product_id        int            primary key auto_increment,
  product_code      varchar(20)    not null,
  category          varchar(50)    not null,
  product_brand     varchar(100)   not null,
  product_name      varchar(100)   not null,
  description       text           not null,
  price             decimal(10,2)  not null,
  discount_percent  decimal(5,2)   default 0.00,
  stock          int            not null,
  stock_status      enum('In Stock', 'Low Stock', 'Out of Stock') as (
        case 
            when stock = 0 then 'Out of Stock'
            when stock between 1 and 5 then 'Low Stock'
            else 'In Stock'
        end
    ) stored,
  product_img 		varchar(255) not null
);

-- Insert Values Into Products 
insert into products (product_code, category, product_brand, product_name, description, price, discount_percent, stock, product_img) values
('JS001', 'Face', 'Jeffree Star', 'Jeffree Star Concealer', 'A full-coverage liquid concealer that provides a smooth, matte finish.', 25.00, 0.00, 20, 'images/JS001.jpg'),
('HU001', 'Face', 'Huda Beauty', 'Huda Beauty Faux Filter Foundation', 'A high-coverage foundation with a soft matte finish and long-lasting wear.', 38.00, 10.00, 10, 'images/HU001.jpg'),
('BE001', 'Eyes', 'Benefit', 'Benefit They\'re Real! Lengthening Mascara', 'Mascara that adds volume and length to lashes with a buildable formula.', 27.00, 5.00, 30, 'images/BE001.jpg'),
('RB001', 'Eyes', 'Rare Beauty', 'Rare Beauty Perfect Strokes Universal Volumizing Mascara', 'A volumizing mascara with a lightweight formula that lifts and curls lashes.',  25.00, 0.00, 15, 'images/RB001.jpg'),
('IT001', 'Eyes', 'IT Cosmetics Superhero Mascara', 'A mascara that creates dramatic volume and length with just one swipe.', 'IT Cosmetics', 26.00, 0.00, 40, 'images/IT001.jpg'),
('HU002', 'Lips', 'Huda Beauty Power Bullet Matte Lipstick', 'A highly pigmented matte lipstick that glides on smoothly and lasts all day.', 'Huda Beauty', 22.00, 10.00, 50, 'images/HU002.jpg'),
('JS002', 'Lips', 'Jeffree Star Velour Liquid Lipstick', 'A highly pigmented, long-lasting liquid lipstick with a matte finish.', 'Jeffree Star', 19.00, 15.00, 25, 'images/JS002.jpg'),
('BE002', 'Accessories', 'Benefit Brow Pencil', 'A precise, easy-to-use brow pencil that fills in brows with natural color.', 'Benefit', 22.00, 0.00, 35, 'images/BE002.jpg'),
('RB002', 'Accessories', 'Rare Beauty Brow Harmony Pencil & Gel', 'A dual-ended pencil and gel for shaping and filling in brows.', 'Rare Beauty', 22.00, 5.00, 18,  'images/RB002.jpg'),
('IT002', 'Accessories', 'IT Cosmetics Heavenly Luxe Brow Power Universal Brow Pencil', 'An award-winning, universal brow pencil that delivers natural-looking brows.', 'IT Cosmetics', 27.00, 0.00, 22, 'images/IT002.jpg'),
('HU003', 'Face', 'Huda Beauty Tantour Cream Contour & Bronzer', 'A creamy contour and bronzer that blends seamlessly for a natural-looking glow.', 'Huda Beauty', 30.00, 0.00, 20, 'images/HU003.jpg'),
('JS003', 'Face', 'Jeffree Star Magic Star Setting Powder', 'A translucent setting powder that mattifies and smooths skin.', 'Jeffree Star', 23.00, 10.00, 50, 'images/JS003.jpg'),
('BE003', 'Face', 'Benefit Benetint Cheek & Lip Stain', 'A rose-tinted stain that gives a natural flush to both lips and cheeks.', 'Benefit', 32.00, 5.00, 15, 'images/BE003.jpg'),
('IT003', 'Face', 'IT Cosmetics Bye Bye Under Eye Concealer', 'A full-coverage concealer that brightens and hides dark circles.', 'IT Cosmetics', 29.00, 0.00, 25, 'images/IT003.jpg'),
('HU004', 'Eyes', 'Huda Beauty Neon Obsessions Palette', 'A vibrant eyeshadow palette with six intense, neon colors.', 'Huda Beauty', 35.00, 0.00, 30, 'images/HU004.jpg'),
('JS004', 'Eyes', 'Jeffree Star Blood Lust Eyeshadow Palette', 'A collection of bold purple and pink eyeshadows with a luxurious finish.', 'Jeffree Star', 54.00, 5.00, 18, 'images/JS004.jpg'),
('BE004', 'Eyes', 'Benefit Roller Lash Mascara', 'A mascara that curls and lengthens lashes with a unique hook-and-loop brush.', 'Benefit', 26.00, 0.00, 40, 'images/BE004.jpg'),
('RB003', 'Eyes', 'Rare Beauty Kind Words Matte Lip Liner', 'A long-wearing lip liner that complements Rare Beauty lipsticks for a perfect pout.', 'Rare Beauty', 18.00, 10.00, 35, 'images/RB003.jpg'),
('IT004', 'Eyes', 'IT Cosmetics Tightline Full Lash Length Black Mascara', 'Mascara that extends and defines lashes with a fine precision brush.', 'IT Cosmetics', 25.00, 0.00, 28, 'images/IT004.jpg'),
('HU005', 'Lips', 'Huda Beauty Liquid Matte Lipstick', 'A long-lasting, matte liquid lipstick with a smooth, velvet finish.', 'Huda Beauty', 23.00, 0.00, 55, 'images/HU005.jpg'),
('JS005', 'Lips', 'Jeffree Star Velour Lip Scrub', 'A lip scrub that exfoliates and hydrates lips with a sweet flavor.', 'Jeffree Star', 12.00, 5.00, 20, 'images/JS005.jpg'),
('BE005', 'Lips', 'Benefit They\'re Real! Double the Lip Lipstick & Liner', 'A two-in-one lipstick and liner that defines and fills lips in one swipe.', 'Benefit', 26.00, 5.00, 18, 'images/BE005.jpg'),
('RB004', 'Lips', 'Rare Beauty Lip Soufflé Matte Lip Cream', 'A soft, mousse-like lip cream that provides a bold, matte finish.', 'Rare Beauty', 24.00, 0.00, 22, 'images/RB004.jpg'),
('IT005', 'Lips', 'IT Cosmetics Your Lips But Better Waterproof Lip Liner Stain', 'A waterproof lip liner that defines lips and stays put all day.', 'IT Cosmetics', 19.00, 0.00, 30, 'images/IT005.jpg'),
('HU006', 'Face', 'Huda Beauty Face & Lip Contour Set', 'A kit that includes everything needed for contouring lips and face with precision.', 'Huda Beauty', 44.00, 10.00, 25, 'images/HU006.jpg'),
('JS006', 'Accessories', 'Jeffree Star Jawbreaker Palette Brush Set', 'A brush set designed for applying eyeshadows and contouring perfectly.', 'Jeffree Star', 35.00, 0.00, 30, 'images/JS006.jpg'),
('BE006', 'Face', 'Benefit Porefessional Primer', 'A pore-minimizing primer that smooths and mattifies skin before makeup application.', 'Benefit', 32.00, 0.00, 40, 'images/BE006.jpg'),
('RB005', 'Lips', 'Rare Beauty Stay Vulnerable Glossy Lip Balm', 'A moisturizing lip balm with a glossy finish that adds a soft tint.', 'Rare Beauty', 20.00, 5.00, 35,  'images/RB005.jpg'),
('IT006', 'Accessories', 'IT Cosmetics Heavenly Luxe Complexion Perfection Brush #7', 'A brush designed for applying foundation, concealer, and powder.', 'IT Cosmetics', 40.00, 5.00, 20, 'images/IT006.jpg'),
('HU007', 'Face', 'Huda Beauty Easy Bake Loose Baking & Setting Powder', 'A loose powder that sets makeup and provides a smooth, matte finish.', 'Huda Beauty', 34.00, 0.00, 45, 'images/HU007.jpg'),
('JS007', 'Face', 'Supreme Frost Highlighter', 'A radiant highlighter for a glowing finish', 'Jeffree Star', 25.99, 0.00, 0, 'images/JS007.jpg'),
('BE007', 'Eyes', 'Badgal Bang Volumising Mascara', 'Mascara for longer and fuller lashes', 'Benefit', 18.50, 0.00, 0, 'images/BE007.jpg'), 
('HU008', 'Eyebrows', '#BOMBBROWS Full ‘n Fluffy Fiber Gel', 'A clear gel to set your brows in place', 'Huda', 15.00, 0.00, 4, 'images/HB008.jpg'),
('RB006', 'Lips', 'Kind Words Lipstick', 'Long-lasting matte finish in a variety of shades', 'Rare Beauty', 22.50, 0.00, 3, 'images/RB006.jpg'), 
('IT007', 'Accessories', 'Makeup Brush Set', 'Complete set of high-quality makeup brushes', 'IT Cosmetics', 49.99, 0.00, 2, 'images/IT007.jpg');

select product_code, product_img from products;
select * from products;

-- Create Customers Table
create table customers (
  customer_id       int            primary key auto_increment,
  first_name        varchar(60)    not null,
  last_name         varchar(60)    not null,
  address1          varchar(100)   not null,
  address2          varchar(100),
  city              varchar(60)    not null,
  eircode           varchar(10),
  phone_no          varchar(15)    not null,
  email_address     varchar(255)   not null  unique,
  account_created   datetime       default current_timestamp,
  is_admin          boolean        default false
);

-- Insert Values Into Customers
insert into customers (first_name, last_name, address1, address2, city, eircode, phone_no, email_address, is_admin) values
  ('admin', 'admin', 'admin', 'admin', 'admin', 'admin', 'admin', 'admin', true),
  ('Adrian', 'Sypos', '456 Main Rd', null, 'Cork', 'T12 3456', '0876543210', 'john.doe@example.com', false),
  ('Monica', 'Jankowska', '789 Oak Rd', 'Apt 5B', 'Limerick', 'V94 5678', '0861234567', 'jane.smith@example.com', false),
  ('Liliana', 'Johnson', '101 Pine Blvd', null, 'Galway', null, '0859876543', 'alice.johnson@example.com', false),
  ('Nicola', 'Brown', '202 Maple St', 'Floor 2', 'Waterford', 'X91 2345', '0871239876', 'bob.brown@example.com', false),
  ('Paul', 'Williams', '303 Birch Lane', null, 'Kilkenny', 'R95 4567', '0892345678', 'charlie.williams@example.com', false),
  ('Emily', 'Davis', '404 Cedar Dr', 'Unit 3', 'Dublin', 'D02 5678', '0868765432', 'emily.davis@example.com', false),
  ('David', 'Miller', '505 Elm St', null, 'Belfast', 'BT1 6789', '0853456789', 'david.miller@example.com', false),
  ('Lidia', 'Dadal', '606 Fir Ave', 'Apt 9', 'Dublin', null, '0876549876', 'sophia.wilson@example.com', false),
  ('Deb', 'Morgan', '707 Cedar Rd', null, 'Cork', 'T12 8901', '0897654321', 'michael.moore@example.com', false);
  
select * from customers;

-- Create Customer Orders Table 
create table orders (
  order_id           int            primary key auto_increment,
  customer_id        int            not null,
  order_date         datetime       not null  default current_timestamp,
  tax_amount         decimal(10,2),
  ship_date          datetime,
  total_order_amount decimal(10,2) ,
  constraint orders_fk_customers
    foreign key (customer_id)
    references customers (customer_id)
);

-- Insert Values Into Customer Orders 
insert into orders (customer_id, order_date, ship_date) values
  (1, '2024-11-01 10:00:00', '2024-11-03 15:00:00'),
  (2, '2024-11-02 12:30:00', '2024-11-05 14:30:00'),
  (3, '2024-11-03 09:15:00', '2024-11-04 16:00:00'),
  (4, '2024-11-04 14:45:00', '2024-11-06 10:30:00'),
  (5, '2024-11-05 16:00:00', '2024-11-07 12:00:00'),
  (6, '2024-11-06 11:20:00', '2024-11-08 09:00:00'),
  (7, '2024-11-07 13:50:00', '2024-11-09 17:00:00'),
  (8, '2024-11-08 10:00:00', '2024-11-10 14:00:00'),
  (10, '2024-11-10 17:00:00', '2024-11-11 18:00:00');
  

-- Create Order Details Table 
create table order_details (
  order_details_id  int            primary key  auto_increment,
  order_id          int            not null,
  product_id        int            not null,
  quantity          int            not null,
  total_item_cost   decimal(10,2)  not null,
  constraint items_fk_orders
    foreign key (order_id)
    references orders (order_id),
  constraint items_fk_products
    foreign key (product_id)
    references products (product_id)
);

/***************************************************
***    Create Triggers For Auto Updating Rows    ***
****************************************************/
-- Create the trigger for tax_amount and total_order_amount in orders table 
drop trigger if exists tr_calculate_order_amounts_after_insert;
delimiter //
create trigger tr_calculate_order_amounts_after_insert 
after insert on order_details
for each row
begin
    declare total_amount decimal(10, 2);
    declare tax_amount decimal(10, 2);
    
    -- Calculate order total cost 
    select sum(total_item_cost) into total_amount from order_details where order_id = NEW.order_id;
    
    -- Calculate tax 
    set tax_amount = total_amount * 0.23;
    
    -- Update orders table with values
    update orders set total_order_amount = total_amount, tax_amount = tax_amount where order_id = new.order_id;
end //
delimiter ;

-- Create the trigger for total_item_cost in order_details table 
drop trigger if exists tr_calculate_total_item_cost_before_insert;
delimiter //
create trigger tr_calculate_total_item_cost_before_insert 
before insert on order_details
for each row
begin
    declare product_price decimal(10, 2);
    select price into product_price from products where product_id = new.product_id;
    set new.total_item_cost = product_price * new.quantity;
end //
delimiter ;

-- Insert Values Into Order Details
insert into order_details (order_id, product_id, quantity) values
  (1, 1, 1), 
  (2, 3, 2), 
  (3, 5, 1), 
  (3, 2, 3),
  (4, 7, 1), 
  (5, 8, 1),  
  (5, 4, 2), 
  (6, 6, 1),  
  (7, 1, 1),
  (8, 9, 2),  
  (8, 4, 3), 
  (9, 7, 1), 
  (9, 2, 1);

select * from customers;
select * from products;
select * from orders;
select * from order_details;

/* 
*   CREATE VIEW
*/
drop view if exists customer_details_view;
create view customer_details_view as select first_name, last_name, address1, address2, city, eircode, phone_no, email_address from customers;
select * from customer_details_view;

/*
*   STORED PROCEDURES
*/
-- Create Order
drop procedure if exists sp_createOrder;
delimiter //
create procedure sp_createOrder(
	in custID int,
    out newOrderID int
)
begin
	declare sql_error tinyint default false;
    declare continue handler for sqlexception
    set sql_error = true;
    
	start transaction;
		insert into orders (customer_id) values (custID);
		set newOrderID = LAST_INSERT_ID();
        if sql_error = false then
			commit;
			select concat("Order ID: ", newOrderID, " was created successfully.") as status;
		else
			rollback;
			select "[ERROR] Couldn't create new order" as status;
		end if;
end // 
delimiter ;

-- Add Product to Order 
drop procedure if exists sp_addProduct;
delimiter //
create procedure sp_addProduct(
	orderID int, 
	productCode varchar(10), 
	amount int
)
begin
	declare productID int;
	declare current_stock int;
    declare productName varchar(50);
    declare sql_error tinyint default false;
    
    declare continue handler for sqlexception
    begin
		set sql_error = true;
        rollback;
	end;
    
    start transaction;
    -- Retrieve product_id and stock amount
	select product_id, product_name, stock into productID, productName, current_stock from products where product_code = productCode;

	if current_stock >= amount then 
		-- Insert into order_details
		insert into order_details (order_id, product_id, quantity) values (orderID, productID, amount);
		-- Update stock 
		update products set stock = stock-amount where product_id = productID;
	end if;

    if sql_error = false then
		commit;
		select concat(amount, "x ", productName) as status;
	else
		rollback;
		select "[ERROR] Couldn't add item to the order" as status;
	end if;
end //
delimiter ;

select * from customers;
select * from products;

call sp_place_order(10, "RB002", 5);
select * from order_details;
select * from orders;