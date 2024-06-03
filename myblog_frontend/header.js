function loadUserInfo() {
    fetch('http://localhost:8080/api/user/current', {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.code === 0 && data.data) {
                const user = data.data;
                document.getElementById('user-avatar').src = `${user.avatarUrl}`;
                document.getElementById('user-name').textContent = user.username;
            } else {
                // 处理用户未登录的情况
                document.getElementById('user-avatar').src = `assets/img/about/4.png`;
                document.getElementById('user-name').textContent = 'Guest';
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            // 处理错误情况
            document.getElementById('user-avatar').src = `assets/img/about/4.png`;
            document.getElementById('user-name').textContent = 'Guest';
        });

    function logout() {
        fetch('http://localhost:8080/api/user/logout', {
            method: 'POST',
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 0) {
                    sessionStorage.removeItem('user');
                    window.location.href = 'index.html';
                } else {
                    alert(data.message);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }
}

// document.addEventListener('DOMContentLoaded', loadUserInfo);
// window.addEventListener('load', loadUserInfo);
loadUserInfo();