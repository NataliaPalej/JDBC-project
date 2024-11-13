/***********************
*   STORED PROCEDURES  *
************************/
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