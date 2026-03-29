document.addEventListener('DOMContentLoaded', () => {
    loadSports();

    const sportForm = document.getElementById('sportForm');
    if (sportForm) {
        sportForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await handleSportSubmit();
        });
    }
});

async function loadSports() {
    try {
        const sports = await apiClient.get('/sports');
        renderSportTable(sports);
    } catch (error) {
        console.error("Error loading sports:", error);
    }
}

function renderSportTable(sports) {
    const tableBody = document.getElementById('sportListTable');
    if (!tableBody) return;

    tableBody.innerHTML = sports.map(sport => `
        <tr>
            <td>${sport.id}</td>
            <td><strong>${sport.name}</strong></td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-warning me-1" onclick="prepareEditSport(${sport.id})">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteSport(${sport.id})">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

async function handleSportSubmit() {
    const form = document.getElementById('sportForm');
    const sportNameValue = document.getElementById('sportName').value;

    const sportData = {
        name: sportNameValue
    };

    const editId = form.dataset.editId;

    try {
        if (editId) {
            await apiClient.put(`/sports/${editId}`, sportData);
        } else {
            await apiClient.post('/sports', sportData);
        }

        resetSportForm(form);
        loadSports();
    } catch (error) {
        console.error("Sport save failed", error);
    }
}

async function prepareEditSport(id) {
    try {
        const sport = await apiClient.get(`/sports/${id}`);
        const form = document.getElementById('sportForm');

        document.getElementById('sportName').value = sport.name;

        form.dataset.editId = id;
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.textContent = "Update Sport";
        submitBtn.classList.replace('btn-primary', 'btn-warning');
    } catch (error) {
        alert("Could not fetch sport details");
    }
}

async function deleteSport(id) {
    if (confirm('Delete this sport category?')) {
        try {
            await apiClient.delete(`/sports/${id}`);
            loadSports();
        } catch (error) {
            alert("Delete failed");
        }
    }
}

function resetSportForm(form) {
    form.reset();
    delete form.dataset.editId;
    const submitBtn = form.querySelector('button[type="submit"]');
    submitBtn.textContent = "Save Sport";
    submitBtn.classList.replace('btn-warning', 'btn-primary');
}