$( document ).ready( function(){
	$( ".contact_name" ).click( function(){
		if ( $( this ).children( ".contact_numbers" ).css( "display" ) == "none" )
			$( this ).children( ".contact_numbers" ).css( "display", "block" );
		else 
			$( this ).children( ".contact_numbers" ).css( "display", "none" );
	});
});