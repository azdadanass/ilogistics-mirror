'use strict';

var usernamePage = document.querySelector('#rusername-page');
var chatPage = document.querySelector('#rchat-page');
var usernameForm = document.querySelector('#rusernameForm');
var messageForm = document.querySelector('#rmessageForm');
var messageInput = document.querySelector('#rmessage');
var messageArea = document.querySelector('#rmessageArea');
var connectingElement = document.querySelector('.rconnecting');



var stompClient = null;
var userList = [];
var privateReceiver = null;

var receiver = null;
var connected =false;


var colors = [
	'#2196F3', '#32c787', '#00BCD4', '#ff5652',
	'#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

//on click on user conversation (to do)
function connect() {


	if (username) {
		usernamePage.classList.add('rhidden');
		chatPage.classList.remove('rhidden');

		var socket = new SockJS('/ilogistics');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, function() {
			// Successfully connected to the WebSocket server
			onConnected();
			// Retrieve latest chat messages from the server

		}, onError);
	}


}


function onConnected() {
	// Subscribe to the Public Topic
	stompClient.subscribe('/topic/public', onMessageReceived);

	// Tell your username to the server
	stompClient.send("/ilogistics/chat.register", {}, JSON.stringify({ sender: username, type: 'JOIN' })
	)

	connectingElement.classList.add('rhidden');
}


function onError(error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}


function send(event) {
	var messageContent = messageInput.value.trim();

	if (messageContent && stompClient) {
		var chatMessage = {
			sender: username,
			receiver: receiver,
			content: messageInput.value,
			type: 'CHAT'
		};

		stompClient.send("/ilogistics/chat.send", {}, JSON.stringify(chatMessage));

		messageInput.value = '';
	}
	event.preventDefault();
}




function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
	var isCurrentUser = (message.sender === username);

	
	if (  (message.receiver === username && message.sender === privateReceiver) || message.sender === username) {
		
		if(message.receiver === username && message.sender === privateReceiver){
			
				callEmptyPutApiWithAjax(privateReceiver);
				
		}
		var messageElement = document.createElement('li');

		if (message.type === 'JOIN') {
			console.log('good');
		} else if (message.type === 'LEAVE') {
			messageElement.classList.add('revent-message');
			message.content = message.sender + ' left!';
		} else {
			messageElement.classList.add('rchat-message');
			if (!isCurrentUser) {
		var avatarElement = document.createElement('img'); // Change 'i' to 'img'
		avatarElement.style.width = '40px'; // Set the width and height for the avatar image
		avatarElement.style.height = '40px';
		
		
		
		$.ajax({
        url: '/chat/photo/' + message.sender,
        type: 'GET',
        success: function(photoUrl) {
            // Set the photo URL as the src attribute of the img element
            
            avatarElement.src=photoUrl;
        },
        error: function(xhr, status, error) {
            console.error('Error fetching user avatar:', error);
        }
    });
		 // Replace with the actual API endpoint to get the user's avatar

		messageElement.appendChild(avatarElement);
	}

			var messageContentElement = document.createElement('div');
			messageContentElement.classList.add('rmessage-content');
			if (isCurrentUser) {
				messageContentElement.style['text-align'] = 'right';
			} else {
				messageContentElement.style['text-align'] = 'left';
			}
			/*var usernameElement = document.createElement('span');
			var usernameText = document.createTextNode(message.sender);
			usernameElement.appendChild(usernameText);
			messageContentElement.appendChild(usernameElement);*/

			var textElement = document.createElement('p');
			textElement.style['line-height'] = '1.25';
			textElement.style['font-size'] = '20px';
			var messageText = document.createTextNode(message.content);
			textElement.appendChild(messageText);
			messageContentElement.appendChild(textElement);

			var timeElement = document.createElement('p');
			timeElement.style['font-size'] = '12px';
			timeElement.style['marginTop'] = '5px';
			var timestamp = formatTimestamp(new Date()); // Format the timestamp from the received message
			var timeText = document.createTextNode(timestamp);
			timeElement.appendChild(timeText);
			messageContentElement.appendChild(timeElement);

			// Add the message content element to the message element
			messageElement.appendChild(messageContentElement);
		}

		messageArea.appendChild(messageElement);
		messageArea.scrollTop = messageArea.scrollHeight;
	}
}

function callEmptyPutApiWithAjax(selecteduser) {
  var apiUrl = '/chat/putSeen/'+selecteduser+'/'+username; // Replace with your API URL

  $.ajax({
    url: apiUrl,
    type: 'PUT',
    success: function(data) {
      // Request successful, handle the response
      console.log(data);
    },
    error: function(xhr, status, error) {
      // Request failed, handle the error
      console.error('Request failed with status:', xhr.status);
    }
  });
}






function getAvatarColor(messageSender) {
	var hash = 0;
	for (var i = 0; i < messageSender.length; i++) {
		hash = 31 * hash + messageSender.charCodeAt(i);
	}

	var index = Math.abs(hash % colors.length);
	return colors[index];
}

function displayMessage(sender, content, timestamp,seen) {
	var isCurrentUser = (sender === username);
	var messageElement = document.createElement('li');
	messageElement.classList.add('rchat-message');
	if (!isCurrentUser) {
		var avatarElement = document.createElement('img'); // Change 'i' to 'img'
		avatarElement.style.width = '40px'; // Set the width and height for the avatar image
		avatarElement.style.height = '40px';
		
		
		
		$.ajax({
        url: '/chat/photo/' + sender,
        type: 'GET',
        success: function(photoUrl) {
            // Set the photo URL as the src attribute of the img element
            
            avatarElement.src=photoUrl;
        },
        error: function(xhr, status, error) {
            console.error('Error fetching user avatar:', error);
        }
    });
		 // Replace with the actual API endpoint to get the user's avatar

		messageElement.appendChild(avatarElement);
	}

	// Rest of the code remains the same
	var messageContentElement = document.createElement('div');
	
	messageContentElement.classList.add('rmessage-content');
	if (isCurrentUser) {
		messageContentElement.style['text-align'] = 'right';
	} else {
		messageContentElement.style['text-align'] = 'left';
	}
	/*if (!isCurrentUser) {
	var usernameElement = document.createElement('span');
	var usernameText = document.createTextNode(sender);
	usernameElement.appendChild(usernameText);
	messageContentElement.appendChild(usernameElement);}*/

	var textElement = document.createElement('p');
	//textElement.style['font-weight']='bold';
	textElement.style['font-size'] = '20px';
	textElement.style['line-height'] = '1.25';
	var messageText = document.createTextNode(content);
	textElement.appendChild(messageText);
	messageContentElement.appendChild(textElement);

	var timeElement = document.createElement('p');
	timeElement.style['font-size'] = '12px';
	timeElement.style['marginTop'] = '5px';
	var timeText = document.createTextNode(formatTimestamp(timestamp));
	timeElement.appendChild(timeText);
	
	if (isCurrentUser && seen) {
		var seenIconElement = document.createElement('i');
		seenIconElement.classList.add('ace-icon', 'fa', 'fa-check-circle', 'smaller-90', 'green');

		timeElement.appendChild(seenIconElement);
	}
	
	messageContentElement.appendChild(timeElement);

	messageElement.appendChild(messageContentElement);

	messageArea.appendChild(messageElement);
	messageArea.scrollTop = messageArea.scrollHeight;
}




// Custom function to format the timestamp
function formatTimestamp(timestamp) {
	var date = new Date(timestamp); // Assuming timestamp is a valid date string or timestamp value

	var today = new Date();
	today.setHours(0, 0, 0, 0); // Set today's date to the beginning of the day

	var yesterday = new Date();
	yesterday.setDate(yesterday.getDate() - 1);
	yesterday.setHours(0, 0, 0, 0); // Set yesterday's date to the beginning of the day

	if (date >= today) {
		// Message is from today, display only the time
		var hours = date.getHours();
		var minutes = date.getMinutes();
		// Add leading zeros to hours and minutes if necessary
		hours = hours.toString().padStart(2, '0');
		minutes = minutes.toString().padStart(2, '0');
		return hours + ':' + minutes;
	} else if (date >= yesterday) {
		// Message is from yesterday, display 'Yesterday' and the time
		var hours = date.getHours();
		var minutes = date.getMinutes();
		// Add leading zeros to hours and minutes if necessary
		hours = hours.toString().padStart(2, '0');
		minutes = minutes.toString().padStart(2, '0');
		return 'Yesterday ' + hours + ':' + minutes;
	} else {
		// Message is from earlier than yesterday, display the date and time
		var year = date.getFullYear();
		var month = date.getMonth() + 1; // Month is zero-based, so add 1
		var day = date.getDate();
		// Add leading zeros to month and day if necessary
		month = month.toString().padStart(2, '0');
		day = day.toString().padStart(2, '0');
		var hours = date.getHours();
		var minutes = date.getMinutes();
		// Add leading zeros to hours and minutes if necessary
		hours = hours.toString().padStart(2, '0');
		minutes = minutes.toString().padStart(2, '0');
		return day + '/' + month + '/' + year + ' ' + hours + ':' + minutes;
	}
}



// Function to retrieve and display the latest chat messages
function retrieveLatestChatMessages(receiver) {
	// Make an AJAX request to the server to get the latest chat messages
	$.ajax({
		type: 'GET',
		url: '/chat/messages/' + username + '/' + receiver, // Replace this with the actual endpoint for getting messages
		success: function(data) {
			// Display the retrieved chat messages in the message area
			data.forEach(function(message) {
				displayMessage(message.sender, message.content, message.timestamp,message.seen);
			});
		},
		error: function() {
			console.log('Error retrieving chat messages.');
		}
	});
}





function startChat(selectedUser) {
	receiver = selectedUser;
	// Hide the username page and show the chat page
	connect();
	// Clear previous chat messages
	messageArea.innerHTML = '';
	// Retrieve latest chat messages for the selected user
	retrieveLatestChatMessages(selectedUser);
	// Connect to WebSocket and start chatting

	$.ajax({
		url: '/chat/users/' + selectedUser,
		method: 'GET',

		success: function(data) {
			var fullName = data; // Assuming the API returns the full name directly
			var chatHeaderPlaceholder = document.getElementById('rchatHeaderPlaceholder');
			chatHeaderPlaceholder.textContent = fullName;
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.error('Error fetching user data:', errorThrown);
			console.log('Response:', jqXHR.responseText); // Log the entire response data for inspection
		}
	});

	$.ajax({
		url: '/chat/users2/' + selectedUser,
		method: 'GET',

		success: function(data) {
			privateReceiver = data; // Assuming the API returns the full name directly
			fetchUserAvatar(privateReceiver);
	        callEmptyPutApiWithAjax(privateReceiver);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.error('Error fetching user data:', errorThrown);
			console.log('Response:', jqXHR.responseText); // Log the entire response data for inspection
		}
	});
	


}
//getPhoto of user 
function fetchUserAvatar(selectedUser) {
    $.ajax({
        url: '/chat/photo/' + selectedUser,
        type: 'GET',
        success: function(photoUrl) {
            // Set the photo URL as the src attribute of the img element
            $('#ruserAvatar').attr('src', photoUrl);
        },
        error: function(xhr, status, error) {
            console.error('Error fetching user avatar:', error);
        }
    });
}



	
	


// Function to filter the user list based on the search input
function filterUserList() {
	var searchInput = document.getElementById('rsearchInput');
	var filter = searchInput.value.toUpperCase();
	var userList = document.querySelectorAll('.ruser-item');

	for (var i = 0; i < userList.length; i++) {
		var username = userList[i].textContent || userList[i].innerText;
		if (username.toUpperCase().indexOf(filter) > -1) {
			userList[i].style.display = '';
		} else {
			userList[i].style.display = 'none';
		}
	}
}

function getUsername(fullName) {
	// Extract the username from the fullName
	var username = fullName.trim().split(/\s+/)[0] + ' ' + fullName.trim().split(/\s+/)[1];
	return username;
}

function updateOnlineStatus() {
	var onlineUsers2 = onlineUsers; // Replace this with the actual online usernames from your API
	var userItems = document.querySelectorAll('#ruser-list .ruser-item');

	userItems.forEach(function(userItem) {
		var username2 = getUsername(userItem.textContent); // Use the getUsername function to get the username
		var statusIndicator = userItem.querySelector('.rstatus-indicator');
		console.log(username2 + ' user selected is here in js');
		if (onlineUsers2.includes(username2)) {
			// User is online, add green circle class
			statusIndicator.classList.add('ronline');
			statusIndicator.classList.remove('roffline');
		} else {
			// User is offline, add red circle class
			statusIndicator.classList.add('roffline');
			statusIndicator.classList.remove('ronline');
		}
	});
}


// Call the function when the page loads and whenever the onlineUsers list is updated
updateOnlineStatus();

function refreshPage() {
	location.reload();
}





messageForm.addEventListener('submit', send, true)
document.getElementById('rsearchInput').addEventListener('keyup', filterUserList);
