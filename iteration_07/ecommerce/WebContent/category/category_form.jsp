<!-- Modal -->
<div class="modal fade" id="categoryModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<form id="categoryForm" method="POST" action="../ws/category/">
			<input type="hidden" id="id" name="id" value="" />
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Category Form</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<label for="name">Name</label> <input type="text"
							class="form-control" id="name" name="name" placeholder="">
					</div>
					<div class="form-group">
						<label for="icon">Icon</label> <input type="text"
							class="form-control" id="icon" name="icon" placeholder="">
					</div>
					<div class="form-group">
						<label for="published">Published</label>
						<!-- selected -->
						<select id="published" name="published" class="form-control">
							<option value="1">Published</option>
							<option value="0">Not published</option>
						</select>
					</div>
				</div>
				<!-- end modal-body -->
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cerrar</button>
					<button type="button" class="btn btn-primary" id="sendJSON">Save
						changes</button>
				</div>
			</div>
			<!-- end modal-content -->
		</form>
	</div>
</div>
