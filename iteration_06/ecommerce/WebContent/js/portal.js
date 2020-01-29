function getCategories(category_id) {
	$.getJSON("../ws/portal/categories", function(result) {
		data = result.data;
		$("#div_categories").empty();
		for (var row = 0; row < data.length; row = row + 1) {
			var id = data[row].id;
			var name = data[row].name;
			var published = data[row].published;
			var icon = data[row].icon;
			var item_class = "list-group-item";
			if (id == category_id) {
				item_class = "list-group-item active";
			}
			$("#div_categories").append(
					"<a href='javascript:getProducts(" + id
							+ ");' id='category_" + id + "' class='"
							+ item_class + "'>" + name + "</a>");
		}
	});
}
function getProducts(category_id) {
	$('.list-group-item').removeClass('active').addClass('');
	$("#category_" + category_id).addClass('active');
	$.getJSON("../ws/portal/products/" + category_id, function(result) {
		data = result.data;
		$("#div_products").empty();
		for (var row = 0; row < data.length; row = row + 1) {
			var id = data[row].id;
			var name = data[row].name;
			var published = data[row].published;
			var icon = data[row].icon;
			var pricing = data[row].pricing;
			var short_description = data[row].short_description;
			var url = "../item/index.jsp?id=" + id;
			var item = '<div class="col-lg-4 col-md-6 mb-4">';
			item += '<div class="card h-100">';
			item += '<a id="link_title" href="' + url + '">';
			item += '<img class="card-img-top" src="../fotos/' + icon
					+ '" alt="">';
			item += '</a>';
			item += '<div class="card-body">';
			item += '<h4 class="card-title">';
			item += '<a href="' + url + '">' + name + '</a>';
			item += '</h4>';
			item += '<h5>$' + pricing + '</h5>';
			item += '<p class="card-text">' + short_description + '</p>';
			
			item += '<a href="javascript:addToCart(' + id + ');" class="btn btn-info" role="button">Buy</a>';
			
			item += '</div>';
			item += '<div class="card-footer">';
			item += '<small class="text-muted">';
			item += '&#9733; &#9733; &#9733; &#9733;&#9734;';
			item += '</small>';
			item += '</div>';
			item += '</div>';
			item += '</div>';
			$("#div_products").append(item);
		}
	});
}

function updateItemsCount(){
	$.getJSON("../ws/cart/items", function(json) {
		var items = json.items;
		$("#shopping_cart").text(items);
	});
}

function addToCart(product_id){
	$.getJSON("../ws/cart/add/" + product_id, function(json) {
		var items = json.items;
		$("#shopping_cart").text(items);
	});
}

