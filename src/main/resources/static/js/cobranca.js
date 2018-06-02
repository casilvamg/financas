$('#confirmacaoExclusaoModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	
	var codigoTitulo = button.data('codigo');
	var descricaoTitulo = button.data('descricao');
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';
	}
	form.attr('action', action + codigoTitulo);
	
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o registro <strong>' + descricaoTitulo + '</strong>?');
});

$('#confirmacaoEmailModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	
	var codigoTitulo = button.data('codigo');
	var email = button.data('email');
	
	var modal = $(this);
	
	console.log(email);
	
	if (!email) { //modal reutilizável
		modal.find('.modal-header h4').html('Aviso');  
		modal.find('.modal-body span').html('Email não cadastrado.'); 
		modal.find(".modal-footer")
		.html("<button class='btn btn-primary' id='btn-modal-update' data-dismiss='modal'>OK</button>"); 
	 }
	else {
		var form = modal.find('form');
		var action = form.data('url-base');
		if (!action.endsWith('/')) {
			action += '/';
		}
		form.attr('action', action + codigoTitulo);
				
		modal.find('.modal-header h4').html('Você tem certeza?'); 
		modal.find('.modal-body span').html('Tem certeza que deseja enviar cobrança para <strong>' + email + '</strong>?');
		modal.find(".modal-footer")
		.html("<button type = 'button' class='btn btn-link' id='btn-modal-cancel' data-dismiss='modal'>Cancelar</button>" +
				" <button type = 'submit' class='btn btn-primary' id='btn-modal-cancel'>Enviar</button>"); 
	
	}
	
});

$(function() {
	$('[rel="tooltip"]').tooltip();
	$('.js-currency').maskMoney({decimal: ',', thousands: '.', allowZero: true});
	
	$('.js-atualizar-status').on('click', function(event) {
		event.preventDefault();
		
		var botaoReceber = $(event.currentTarget);
		var urlReceber = botaoReceber.attr('href');
		
		var response = $.ajax({
			url: urlReceber,
			type: 'PUT'
		});		
		
		response.done(function(e) {
			var codigoTitulo = botaoReceber.data('codigo');

			$('[data-role=' + codigoTitulo + ']').html('<span class="label label-success">' + e + '</span>');
			botaoReceber.hide();
			$("#bt_email").hide();
			location.reload();
		});
		
		response.fail(function(e) {
			console.log(e);
			alert('Erro recebendo cobrança');
		});
		
	});
});
