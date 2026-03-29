document.addEventListener('DOMContentLoaded', () => {
    loadCompetitions();
    loadSportsForDropdown();

    const compForm = document.getElementById('competationForm');
    if (compForm) {
        compForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await handleCompetitionSubmit();
        });
    }
});

async function loadSportsForDropdown() {
    const dropdown = document.getElementById('compSportId');
    try {
        const sports = await apiClient.get('/sports');
        dropdown.innerHTML = '<option value="" selected disabled>Select a Sport</option>' +
            sports.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
    } catch (error) {
        dropdown.innerHTML = '<option value="">Error loading sports</option>';
    }
}

async function loadCompetitions() {
    try {
        const competitions = await apiClient.get('/competitions');
        renderCompetitionTable(competitions);
    } catch (error) {
        console.error("Error loading competitions:", error);
    }
}

function renderCompetitionTable(competitions) {
    const tableBody = document.getElementById('competationListTable');
    if (!tableBody) return;

    tableBody.innerHTML = competitions.map(c => `
        <tr>
            <td><small class="text-muted">${c.id}</small></td>
            <td><strong>${c.name}</strong></td>
            <td><span class="badge bg-info text-dark">${c.sportName || 'N/A'}</span></td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-warning me-1" onclick="prepareEditCompetition('${c.id}')">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteCompetition('${c.id}')">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

async function handleCompetitionSubmit() {
    const form = document.getElementById('competationForm');

    const compData = {
        id: document.getElementById('compId').value,
        name: document.getElementById('compName').value,
        sportId: parseInt(document.getElementById('compSportId').value)
    };

    const editId = form.dataset.editId;

    try {
        if (editId) {
            await apiClient.put(`/competitions/${editId}`, compData);
        } else {
            await apiClient.post('/competitions', compData);
        }

        resetForm('competationForm');
        document.getElementById('compId').disabled = false;
        loadCompetitions();
    } catch (error) {
    }
}

async function prepareEditCompetition(id) {
    try {
        const comp = await apiClient.get(`/competitions/${id}`);
        const form = document.getElementById('competationForm');

        document.getElementById('compId').value = comp.id;
        document.getElementById('compId').disabled = true;
        document.getElementById('compName').value = comp.name;

        form.dataset.editId = id;
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.textContent = "Update Competition";
        submitBtn.classList.replace('btn-primary', 'btn-warning');
    } catch (error) {
        alert("Could not fetch competition details");
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadCompetitions();
    loadSportsForDropdown();

    const compForm = document.getElementById('competationForm');
    if (compForm) {
        compForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await handleCompetitionSubmit();
        });
    }
});

async function loadSportsForDropdown() {
    const dropdown = document.getElementById('compSportId');
    try {
        const sports = await apiClient.get('/sports');
        dropdown.innerHTML = '<option value="" selected disabled>Select a Sport</option>' +
            sports.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
    } catch (error) {
        dropdown.innerHTML = '<option value="">Error loading sports</option>';
    }
}

async function loadCompetitions() {
    try {
        const competitions = await apiClient.get('/competitions');
        renderCompetitionTable(competitions);
    } catch (error) {
        console.error("Error loading competitions:", error);
    }
}

function renderCompetitionTable(competitions) {
    const tableBody = document.getElementById('competationListTable');
    if (!tableBody) return;

    tableBody.innerHTML = competitions.map(c => `
        <tr>
            <td><small class="text-muted">${c.id}</small></td>
            <td><strong>${c.name}</strong></td>
            <td><span class="badge bg-info text-dark">${c.sportName || 'N/A'}</span></td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-warning me-1" onclick="prepareEditCompetition('${c.id}')">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteCompetition('${c.id}')">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

async function handleCompetitionSubmit() {
    const form = document.getElementById('competationForm');

    const compData = {
        id: document.getElementById('compId').value,
        name: document.getElementById('compName').value,
        sportId: parseInt(document.getElementById('compSportId').value)
    };

    const editId = form.dataset.editId;

    try {
        if (editId) {
            await apiClient.put(`/competitions/${editId}`, compData);
        } else {
            await apiClient.post('/competitions', compData);
        }

        await loadCompetitions();

        resetForm('competationForm');

        document.getElementById('compId').disabled = false;
    } catch (error) {
    }
}

async function prepareEditCompetition(id) {
    try {
        const comp = await apiClient.get(`/competitions/${id}`);
        const form = document.getElementById('competationForm');

        document.getElementById('compId').value = comp.id;
        document.getElementById('compId').disabled = true;
        document.getElementById('compName').value = comp.name;

        form.dataset.editId = id;

        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.textContent = "Update Competition";
        submitBtn.classList.replace('btn-primary', 'btn-warning');
    } catch (error) {
        alert("Could not fetch competition details");
    }
}

async function deleteCompetition(id) {
    if (confirm('Are you sure you want to delete this competition?')) {
        try {
            await apiClient.delete(`/competitions/${id}`);
            await loadCompetitions();
        } catch (error) {
            alert("Delete failed");
        }
    }
}