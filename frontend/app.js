const apiBaseUrl = 'http://192.168.152.128:8080/api/account-book';

document.addEventListener("DOMContentLoaded",async () =>{
    const loginButton = document.getElementById("login-button");
    const navButtons = document.getElementById("nav-buttons");
    const incomeSection = document.getElementById("income-section");
    const expenseSection = document.getElementById("expense-section");

    try{
        const response = await fetch("http://192.168.152.128:8080/api/auth/status",{
            method: "GET",
            credentials: "include"
        });

        if(response.ok){
            const username = await response.text();
            // 로그인 상태: 버튼을 로그아웃으로 변경
            loginButton.textContent=`로그아웃 (${username})`;
            loginButton.classList.remove("btn-primary");
            loginButton.classList.add("btn-danger");
            loginButton.href="#";

            loginButton.addEventListener("click",async(event) => {
                event.preventDefault();
                await logout();
            });

             // 콘텐츠 표시
            navButtons.style.display = "flex";
            incomeSection.style.display = "block";
            expenseSection.style.display = "block";
            fetchEntries();

        }else{
            // 로그아웃 상태: 로그인 페이지로 이동
            loginButton.textContent = "로그인";
            loginButton.classList.remove("btn-danger");
            loginButton.classList.add("btn-primary");
            loginButton.href = "login.html";
        }
    }catch (error){
        console.error("로그인 상태 확인 중 오류:",error);
    }
})

async function logout(){
    try{
        const response = await fetch("http://192.168.152.128:8080/api/auth/logout",{
            method: "GET",
            credentials: "include"
        });

        if(response.ok){
            alert("로그아웃 성공");
            location.reload();
        }else{
            alert("로그아웃 실패");
        }
    }catch(error){
        console.error("로그아웃 요청 중 오류:",error);
    }
}


// Fetch and display all account book entries
async function fetchEntries() {
    const response = await fetch(apiBaseUrl,{
        method: "GET",
        credentials: "include"
    });
    const entries = await response.json();

    const incomeList = document.getElementById('income-list');
    const expenseList = document.getElementById('expense-list');

    incomeList.innerHTML = ''; // Clear existing rows
    expenseList.innerHTML = '';

    entries.forEach(entry => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${entry.date}</td>
            <td>${entry.amount.toLocaleString()}</td>
            <td>${entry.category}</td>
            <td>${entry.description}</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editEntry(${entry.id})">수정</button>
                <button class="btn btn-danger btn-sm" onclick="deleteEntry(${entry.id})">삭제</button>
            </td>
        `;

        if (entry.type === 'INCOME') {
            incomeList.appendChild(row);
        } else if (entry.type === 'EXPENSE') {
            expenseList.appendChild(row);
        }
    });
}

// Delete a specific entry
async function deleteEntry(id) {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    await fetch(`${apiBaseUrl}/${id}`, { method: 'DELETE' });
    fetchEntries(); // Refresh the list
}

// Delete all entries
document.getElementById('delete-all').addEventListener('click', async () => {
    if (!confirm('정말로 삭제하시겠습니까?')) return;

    await fetch(apiBaseUrl, { method: 'DELETE' ,
        credentials: "include"
    });
    fetchEntries(); // Refresh the list
});

// Edit an entry (redirect to edit page)
function editEntry(id) {
    window.location.href = `edit.html?id=${id}`;
}

// Initialize the page by fetching and displaying entries
fetchEntries();
