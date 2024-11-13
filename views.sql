/* *****************
*      VIEWS       *
********************/
drop view if exists customer_details_view;
create view customer_details_view as select first_name, last_name, address1, address2, city, eircode, phone_no, email_address from customers;
select * from customer_details_view;

 drop view if exists view_orders;
 create view view_orders as select o.order_id as "Order ID", o.order_date as "Order Date", p.product_code as "Product Code", p.product_name as "Product Name", od.quantity as "Quantity",
	od.total_item_cost as "Total Product Cost", p.discount_percent as "Discount",  (od.total_item_cost * (1 - (p.discount_percent / 100))) as "Discounted Price", o.tax_amount as "Tax Amount", 
    o.total_order_amount as "Total" from order_details od inner join products p on od.product_id = p.product_id inner join orders o on od.order_id = o.order_id where o.order_id = o.order_id;

-- View used to get details for each individual order 
drop view if exists customer_orders_view;
create view customer_orders_view as select c.customer_id, o.order_id, o.order_date, p.product_code, p.product_name, od.quantity, od.total_item_cost, o.tax_amount, o.total_order_amount from order_details od 
join products p on od.product_id = p.product_id join orders o on od.order_id = o.order_id join customers c on o.customer_id = c.customer_id;

drop view if exists customer_delivery_details_view;
create view customer_delivery_details_view as select customer_id, first_name, last_name, address1, address2, city, eircode, phone_no, email_address from customers;

select * from customer_delivery_details_view;
select * from customer_orders_view where customer_id = 8;

drop view if exists sales_by_category_view;
create view sales_by_category_view as select p.category as "Category", SUM(od.quantity) as "Quantity Sold", SUM(od.total_item_cost) as "Total Sales" 
from order_details od join products p on od.product_id = p.product_id group by p.category;

drop view if exists sales_by_brand_view;
create view sales_by_brand_view as select p.product_brand as "Brand", SUM(od.quantity) as "Quantity Sold", SUM(od.total_item_cost) as "Total Sales"
from order_details od join products p on od.product_id = p.product_id group by p.product_brand;

drop view if exists sales_by_product_view;
create view sales_by_product_view as select p.product_name as "Name", SUM(od.quantity) as "Quantity Sold", SUM(od.total_item_cost) as "Total Sales"
from order_details od join products p on od.product_id = p.product_id group by p.product_name;