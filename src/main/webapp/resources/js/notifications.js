/**
 * 
 */

var stompClient2 = null;









function connect2() {


	if (username2) {


		var socket = new SockJS('/ilogistics');
		stompClient2 = Stomp.over(socket);

		stompClient2.connect({}, function() {
			// Successfully connected to the WebSocket server
			onConnected2();
			// Retrieve latest chat messages from the server

		}, onError2);
	}


}


function onConnected2() {
	// Subscribe to the Public Topic
	stompClient2.subscribe('/topic/public', onMessageReceived2);

	// Tell your username to the server
	stompClient2.send("/ilogistics/chat.register", {}, JSON.stringify({ sender: username2, type: 'JOIN' })
	)


}


function onError2(error) {

}

function showNotification2(summary, detail, type = 'info') {

	Toastify({
		text: detail,
		duration: 5000, // Time in milliseconds the notification will be displayed (5 seconds in this example)
		close: true,
		gravity: 'top', // Position of the notification on the screen
		position: 'right',
		backgroundColor: type === 'error' ? '#ff6347' : '#ff5050',
		className: 'custom-toast', // Customize the background color based on the notification type
	}).showToast();
}

function getURLParameter(name) {
	const params = new URLSearchParams(window.location.search);
	return params.get(name);
}

function getCurrentXHTMLPageName() {
	const path = window.location.pathname;
	const lastSlashIndex = path.lastIndexOf('/');
	const pageNameWithExtension = path.substring(lastSlashIndex + 1);
	const pageName = pageNameWithExtension.replace('.xhtml', '');
	return pageName;
}

function onMessageReceived2(payload) {
	var message = JSON.parse(payload.body);
	const urlParam = getURLParameter('username');

	function refreshComponent33() {
		var componentId = 'header_form';
		var element = document.getElementById(componentId);

		if (element) {
			jsf.ajax.request(
				element,
				null,
				{
					execute: componentId,
					render: componentId
				}
			);
		} else {
			console.error("Component with ID '" + componentId + "' not found.");
		}
	}



	if (message.receiver === username2 && message.sender != urlParam) {


		$.ajax({
			url: '/chat/users/' + message.sender,
			method: 'GET',

			success: function(data) {
				var summary = "New Message";
				var detail = "You have received a new message from " + data + " !";
				var type = "info"; // You can use 'error', 'warning', 'success', or 'info' for different notification types
				showNotification2(summary, detail, type);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				console.error('Error fetching user data:', errorThrown);
				console.log('Response:', jqXHR.responseText); // Log the entire response data for inspection
			}
		});



	}



}





document.addEventListener("DOMContentLoaded", function() {
	connect2();
});