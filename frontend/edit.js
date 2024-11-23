const apiBaseUrl = 'http://192.168.152.128:8080/api/account-book';

// URL에서 ID 추출
const urlParams = new URLSearchParams(window.location.search);
const entryId = urlParams.get('id');

// 페이지 로드 시 데이터 불러오기
async function fetchEntry() {
    try {
        const response = await fetch(`${apiBaseUrl}/${entryId}`);
        if (!response.ok) {
            throw new Error('수정에 실패했습니다.');
        }

        const entry = await response.json();

        // 데이터를 입력 필드에 채워 넣기
        document.getElementById('entry-id').value = entry.id;
        document.getElementById('date').value = entry.date;
        document.getElementById('amount').value = entry.amount;
        document.getElementById('category').value = entry.category;
        document.getElementById('description').value = entry.description;
        document.getElementById('type').value = entry.type;
    } catch (error) {
        console.error('Error fetching entry:', error);
    }
}

// 수정 요청 처리
document.getElementById('edit-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const updatedEntry = {
        id: document.getElementById('entry-id').value,
        date: document.getElementById('date').value,
        amount: document.getElementById('amount').value,
        category: document.getElementById('category').value,
        description: document.getElementById('description').value,
        type: document.getElementById('type').value,
    };

    try {
        const response = await fetch(`${apiBaseUrl}/${entryId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedEntry),
        });

        if (!response.ok) {
            throw new Error('수정이 실패했습니다.');
        }

        alert('수정이 완료됐습니다.');
        window.location.href = 'account-book.html'; // 수정 완료 후 목록으로 이동
    } catch (error) {
        console.error('수정 실패:', error);
        alert('수정이 실패했습니다. 다시 시도해주세요.');
    }
});

// 데이터 불러오기 실행
fetchEntry();
