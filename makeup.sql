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
  stock_status      enum('In Stock', 'Low Stock', 'Out of Stock') not null,
  product_img 		varchar(255) not null
);

-- Insert Values Into Products 
insert into products (product_code, category, product_brand, product_name, description, price, discount_percent, stock, stock_status, product_img) values
('JS001', 'Face', 'Jeffree Star', 'Jeffree Star Concealer', 'A full-coverage liquid concealer that provides a smooth, matte finish.', 25.00, 0.00, 20, 'In Stock', 'https://jeffreestarcosmetics.com/products/magic-star-concealer'),
('HU001', 'Face', 'Huda Beauty', 'Huda Beauty Faux Filter Foundation', 'A high-coverage foundation with a soft matte finish and long-lasting wear.', 38.00, 10.00, 10, 'Low Stock', 'https://hudabeauty.com/us/en_US/foundation/fauxfilter-luminous-matte-foundation-HB00509M.html'),
('BE001', 'Eyes', 'Benefit', 'Benefit They\'re Real! Lengthening Mascara', 'Mascara that adds volume and length to lashes with a buildable formula.', 27.00, 5.00, 30, 'In Stock', 'https://www.benefitcosmetics.com/en-gb/product/theyre-real-lengthening-mascara'),
('RB001', 'Eyes', 'Rare Beauty', 'Rare Beauty Perfect Strokes Universal Volumizing Mascara', 'A volumizing mascara with a lightweight formula that lifts and curls lashes.',  25.00, 0.00, 15, 'In Stock', 'https://www.rarebeauty.com/products/perfect-strokes-universal-volumizing-mascara'),
('IT001', 'Eyes', 'IT Cosmetics Superhero Mascara', 'A mascara that creates dramatic volume and length with just one swipe.', 'IT Cosmetics', 26.00, 0.00, 40, 'In Stock', 'https://www.itcosmetics.com/superhero-mascara/ITC_622.html'),
('HU002', 'Lips', 'Huda Beauty Power Bullet Matte Lipstick', 'A highly pigmented matte lipstick that glides on smoothly and lasts all day.', 'Huda Beauty', 22.00, 10.00, 50, 'In Stock', 'https://hudabeauty.com/us/en_US/power-bullet-matte-lipstick-HB00358.html'),
('JS002', 'Lips', 'Jeffree Star Velour Liquid Lipstick', 'A highly pigmented, long-lasting liquid lipstick with a matte finish.', 'Jeffree Star', 19.00, 15.00, 25, 'Low Stock', 'https://jeffreestarcosmetics.com/collections/velour-liquid-lipstick'),
('BE002', 'Accessories', 'Benefit Brow Pencil', 'A precise, easy-to-use brow pencil that fills in brows with natural color.', 'Benefit', 22.00, 0.00, 35, 'In Stock', 'https://www.benefitcosmetics.com/en-gb/product/goof-proof-brow-pencil'),
('RB002', 'Accessories', 'Rare Beauty Brow Harmony Pencil & Gel', 'A dual-ended pencil and gel for shaping and filling in brows.', 'Rare Beauty', 22.00, 5.00, 18, 'In Stock', 'https://www.rarebeauty.com/products/brow-harmony-pencil-gel'),
('IT002', 'Accessories', 'IT Cosmetics Heavenly Luxe Brow Power Universal Brow Pencil', 'An award-winning, universal brow pencil that delivers natural-looking brows.', 'IT Cosmetics', 27.00, 0.00, 22, 'In Stock', 'https://www.itcosmetics.com/heavenly-luxe-brow-power-universal-brow-transformer-brush/ITC_693.html'),
('HU003', 'Face', 'Huda Beauty Tantour Cream Contour & Bronzer', 'A creamy contour and bronzer that blends seamlessly for a natural-looking glow.', 'Huda Beauty', 30.00, 0.00, 20, 'Low Stock', 'https://hudabeauty.com/us/en_US/tantour-bronzer-cream-HB00360.html'),
('JS003', 'Face', 'Jeffree Star Magic Star Setting Powder', 'A translucent setting powder that mattifies and smooths skin.', 'Jeffree Star', 23.00, 10.00, 50, 'In Stock', 'https://jeffreestarcosmetics.com/products/magic-star-setting-powder'),
('BE003', 'Face', 'Benefit Benetint Cheek & Lip Stain', 'A rose-tinted stain that gives a natural flush to both lips and cheeks.', 'Benefit', 32.00, 5.00, 15, 'In Stock', 'https://www.benefitcosmetics.com/en-gb/product/benetint-cheek-lip-stain'),
('IT003', 'Face', 'IT Cosmetics Bye Bye Under Eye Concealer', 'A full-coverage concealer that brightens and hides dark circles.', 'IT Cosmetics', 29.00, 0.00, 25, 'Low Stock', 'https://www.itcosmetics.com/bye-bye-under-eye-full-coverage-anti-aging-waterproof-concealer/ITC_0005.htm'),
('HU004', 'Eyes', 'Huda Beauty Neon Obsessions Palette', 'A vibrant eyeshadow palette with six intense, neon colors.', 'Huda Beauty', 35.00, 0.00, 30, 'In Stock', 'https://hudabeauty.com/us/en_US/neon-obsessions-palette-HB00361.html'),
('JS004', 'Eyes', 'Jeffree Star Blood Lust Eyeshadow Palette', 'A collection of bold purple and pink eyeshadows with a luxurious finish.', 'Jeffree Star', 54.00, 5.00, 18, 'Low Stock', 'https://jeffreestarcosmetics.com/products/blood-lust-palette'),
('BE004', 'Eyes', 'Benefit Roller Lash Mascara', 'A mascara that curls and lengthens lashes with a unique hook-and-loop brush.', 'Benefit', 26.00, 0.00, 40, 'In Stock', 'https://www.benefitcosmetics.com/en-gb/product/roller-lash'),
('RB003', 'Eyes', 'Rare Beauty Kind Words Matte Lip Liner', 'A long-wearing lip liner that complements Rare Beauty lipsticks for a perfect pout.', 'Rare Beauty', 18.00, 10.00, 35, 'In Stock', 'https://www.rarebeauty.com/products/kind-words-matte-lip-liner'),
('IT004', 'Eyes', 'IT Cosmetics Tightline Full Lash Length Black Mascara', 'Mascara that extends and defines lashes with a fine precision brush.', 'IT Cosmetics', 25.00, 0.00, 28, 'In Stock', 'https://www.itcosmetics.com/tightline-3-in-1-black-primer-eyeliner-mascara/ITC_506.html'),
('HU005', 'Lips', 'Huda Beauty Liquid Matte Lipstick', 'A long-lasting, matte liquid lipstick with a smooth, velvet finish.', 'Huda Beauty', 23.00, 0.00, 55, 'In Stock', 'https://hudabeauty.com/us/en_US/liquid-matte-lipstick-HB00362.html'),
('JS005', 'Lips', 'Jeffree Star Velour Lip Scrub', 'A lip scrub that exfoliates and hydrates lips with a sweet flavor.', 'Jeffree Star', 12.00, 5.00, 20, 'In Stock', 'https://jeffreestarcosmetics.com/collections/velour-lip-scrub'),
('BE005', 'Lips', 'Benefit They\'re Real! Double the Lip Lipstick & Liner', 'A two-in-one lipstick and liner that defines and fills lips in one swipe.', 'Benefit', 26.00, 5.00, 18, 'Low Stock', 'https://www.benefitcosmetics.com/en-gb/product/theyre-real-double-the-lip'),
('RB004', 'Lips', 'Rare Beauty Lip Soufflé Matte Lip Cream', 'A soft, mousse-like lip cream that provides a bold, matte finish.', 'Rare Beauty', 24.00, 0.00, 22, 'In Stock', 'https://www.rarebeauty.com/products/lip-souffle-matte-lip-cream'),
('IT005', 'Lips', 'IT Cosmetics Your Lips But Better Waterproof Lip Liner Stain', 'A waterproof lip liner that defines lips and stays put all day.', 'IT Cosmetics', 19.00, 0.00, 30, 'Low Stock', 'https://www.itcosmetics.com/your-lips-but-better-waterproof-lip-liner-stain/ITC_0014.html'),
('HU006', 'Face', 'Huda Beauty Face & Lip Contour Set', 'A kit that includes everything needed for contouring lips and face with precision.', 'Huda Beauty', 44.00, 10.00, 25, 'In Stock', 'https://hudabeauty.com/us/en_US/face-lip-contour-set-HB00363.html'),
('JS006', 'Accessories', 'Jeffree Star Jawbreaker Palette Brush Set', 'A brush set designed for applying eyeshadows and contouring perfectly.', 'Jeffree Star', 35.00, 0.00, 30, 'In Stock', 'https://jeffreestarcosmetics.com/products/jawbreaker-palette-brush-set'),
('BE006', 'Face', 'Benefit Porefessional Primer', 'A pore-minimizing primer that smooths and mattifies skin before makeup application.', 'Benefit', 32.00, 0.00, 40, 'In Stock', 'https://www.benefitcosmetics.com/en-gb/product/porefessional-face-primer'),
('RB005', 'Lips', 'Rare Beauty Stay Vulnerable Glossy Lip Balm', 'A moisturizing lip balm with a glossy finish that adds a soft tint.', 'Rare Beauty', 20.00, 5.00, 35, 'Low Stock', 'https://www.rarebeauty.com/products/stay-vulnerable-glossy-lip-balm'),
('IT006', 'Accessories', 'IT Cosmetics Heavenly Luxe Complexion Perfection Brush #7', 'A brush designed for applying foundation, concealer, and powder.', 'IT Cosmetics', 40.00, 5.00, 20, 'In Stock', 'https://www.itcosmetics.com/heavenly-luxe-complexion-perfection-brush-7/ITC_524.html'),
('HU007', 'Face', 'Huda Beauty Easy Bake Loose Baking & Setting Powder', 'A loose powder that sets makeup and provides a smooth, matte finish.', 'Huda Beauty', 34.00, 0.00, 45, 'In Stock', 'https://hudabeauty.com/us/en_US/easy-bake-loose-baking-setting-powder-HB00364.html'),
('P007', 'Face', 'Glow Highlighter', 'A radiant highlighter for a glowing finish', 'Jeffree Star', 25.99, 0.00, 0, 'Out of Stock', 'https://jeffreestarcosmetics.com/products/supreme-frost-highlighter'),
('P008', 'Eyes', 'Mascara Volume', 'Mascara for longer and fuller lashes', 'Benefit', 18.50, 0.00, 0, 'Out of Stock', 'https://www.benefitcosmetics.com/en-gb/product/badgal-bang-volumizing-mascara'), 
('P009', 'Eyebrows', 'Eyebrow Gel', 'A clear gel to set your brows in place', 'Huda', 15.00, 0.00, 4, 'In Stock', 'https://hudabeauty.com/us/en_US/bombbrows-full-n-fluffy-fiber-gel-HB00365.html'),
('P010', 'Lips', 'Matte Lipstick', 'Long-lasting matte finish in a variety of shades', 'Rare Beauty', 22.50, 0.00, 3, 'In Stock', 'https://www.rarebeauty.com/products/lip-souffle-matte-lip-cream'), 
('P011', 'Accessories', 'Makeup Brush Set', 'Complete set of high-quality makeup brushes', 'IT Cosmetics', 49.99, 0.00, 2, 'In Stock', 'https://www.itcosmetics.com/heavenly-luxe-6-piece-brush-set/ITC_0015.html');

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
  ship_amount        decimal(10,2)  not null default 5.00,
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
drop trigger if exists tr_calculate_order_amounts_before_insert;

delimiter //
create trigger tr_calculate_order_amounts_after_insert 
after insert on order_details
for each row
begin
    declare total_amount decimal(10, 2);
    declare product_price decimal(10, 2);
    declare total_quantity int;
    declare tax_amount decimal(10, 2);
    
    -- Get the total quantity for the order
    select sum(quantity) into total_quantity from order_details where order_id = new.order_id;

    -- Get product price     
    select price into product_price from products where product_id = new.product_id;

    -- Calculate tax_amount
    set tax_amount = product_price * total_quantity * 0.23;
    
    -- Calculate total_order_amount
    set total_amount = (product_price * total_quantity) + tax_amount + (select ship_amount from orders where order_id = new.order_id);
    
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