alter table mooc_products add column sku varchar(255) null;
alter table mooc_products add constraint mooc_products_sku_uindex unique (sku);
alter table mooc_products alter column `sku` varchar(255) not null;