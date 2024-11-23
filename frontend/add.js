const apiBaseUrl = 'http://192.168.152.128:8080/api/account-book';

document.getElementById('add-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const newEntry = {
        date: document.getElementById('date').value,
        amount: document.getElementById('amount').value,
        category: document.getElementById('category').value,
        description: document.getElementById('description').value,
        type: document.getElementById('type').value,
    };

    await fetch(apiBaseUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newEntry),
        credentials: "include"
    });

    alert('내역이 추가됐습니다.');
    window.location.href = 'account-book.html'; // Redirect to list
});
