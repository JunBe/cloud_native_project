const apiBaseUrl = 'http://192.168.152.128:8080/api/statistics';

document.getElementById('statistics-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const year = document.getElementById('year').value;
    const month = document.getElementById('month').value;

    try {
        const response = await fetch(`${apiBaseUrl}?year=${year}&month=${month}`,
            {method: "GET",
            credentials: "include"}
        );
        if (!response.ok) {
            throw new Error('응답에 실패했습니다.');
        }

        const data = await response.json();
        renderStatistics(data);
    } catch (error) {
        console.error('응답에 실패했습니다.:', error);
    }
});

function renderStatistics(data) {
    const incomeTable = document.getElementById('income-statistics');
    const expenseTable = document.getElementById('expense-statistics');
    const incomeTotal = document.getElementById('income-total');
    const expenseTotal = document.getElementById('expense-total');
    const totalBalance = document.getElementById('total-balance');

    // 통계 데이터 초기화
    incomeTable.innerHTML = '';
    expenseTable.innerHTML = '';
    incomeTotal.innerText = '';
    expenseTotal.innerText = '';
    totalBalance.innerText = '';

    // 수입 데이터 추가
    const incomeStats = data.incomeStatistics;
    incomeStats.forEach(stat => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${stat.category}</td>
            <td>${stat.amount.toLocaleString()}</td>
            <td>${stat.percentage.toFixed(2)}%</td>
        `;
        incomeTable.appendChild(row);
    });

    // 지출 데이터 추가
    const expenseStats = data.expenseStatistics;
    expenseStats.forEach(stat => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${stat.category}</td>
            <td>${stat.amount.toLocaleString()}</td>
            <td>${stat.percentage.toFixed(2)}%</td>
        `;
        expenseTable.appendChild(row);
    });

    // 전체 통계
    incomeTotal.innerText = `수입: ${data.incomeTotalAmount.toLocaleString()}`;
    expenseTotal.innerText = `지출 ${data.expenseTotalAmount.toLocaleString()}`;
    totalBalance.innerText = `총 지출 ${data.totalAmount.toLocaleString()}`;
}
