        var socket = new SockJS('/ws');
        var stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/changes', function (message) {
                console.log('received message:' + message.body);
                showNotification(message.body);
            });
        });

        function showNotification(message) {
            var notificationContainer = document.getElementById('notification-container');

            // Utwórz nowy element powiadomienia
            var notificationElement = document.createElement('div');
            notificationElement.className = 'notification';
            notificationElement.style.border = '1px solid #ccc';
            notificationElement.style.background = '#f8f8f8';
            notificationElement.style.padding = '10px';
            notificationElement.style.marginBottom = '5px';
            notificationElement.style.borderRadius = '5px';
            notificationElement.style.position = 'relative';

            // Dodaj tekst powiadomienia
            var messageElement = document.createElement('span');
            messageElement.innerText = message;
            notificationElement.appendChild(messageElement);

            // Utwórz przycisk zamknięcia
            var closeButton = document.createElement('button');
            closeButton.innerText = '×';
            closeButton.style.position = 'absolute';
            closeButton.style.top = '5px';
            closeButton.style.right = '5px';
            closeButton.style.border = 'none';
            closeButton.style.background = 'transparent';
            closeButton.style.fontSize = '16px';
            closeButton.style.cursor = 'pointer';

            closeButton.onclick = function () {
                notificationContainer.removeChild(notificationElement);
            };

            notificationElement.appendChild(closeButton);
            notificationContainer.appendChild(notificationElement);
        }
