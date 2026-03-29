document.addEventListener('DOMContentLoaded', () => {
    loadTeams();

    const teamForm = document.getElementById('teamRegistrationForm');
    teamForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        await handleTeamSubmit();
    });
});

async function loadTeams() {
    try {
        const teams = await apiClient.get('/teams');
        renderTeamTable(teams);
    } catch (error) {
        console.error("Error loading teams:", error);
    }
}

function renderTeamTable(teams) {
    const tableBody = document.getElementById('teamListTable');
    tableBody.innerHTML = teams.map(team => `
        <tr>
            <td>
                <strong>${team.name}</strong><br>
                <small class="text-muted">${team.officialName}</small>
            </td>
            <td>${team.slug}<br><span class="badge bg-secondary">${team.abbreviation}</span></td>
            <td>${team.teamCountryCode}</td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-warning me-1" onclick="prepareEditTeam(${team.id})">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteTeam(${team.id})">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

async function handleTeamSubmit() {
    const form = document.getElementById('teamRegistrationForm');

    const teamData = {
        name: document.getElementById('teamName').value,
        officialName: document.getElementById('officialName').value,
        slug: document.getElementById('teamSlug').value,
        abbreviation: document.getElementById('abbreviation').value,
        teamCountryCode: document.getElementById('countryCode').value,
        stagePosition: parseInt(document.getElementById('stagePos').value) || 1
    };

    const editId = form.dataset.editId;

    try {
        if (editId) {
            await apiClient.put(`/teams/${editId}`, teamData);
        } else {
            await apiClient.post('/teams', teamData);
        }

        resetTeamForm(form);
        loadTeams();

    } catch (error) {
        const data = error.response?.data;

        if (data?.errors) {
            showTeamErrors(data.errors);
        } else if (data?.message) {
            alert(data.message);
        } else {
            alert("Unexpected error occurred");
        }
    }
}

function showTeamErrors(errors) {
    const fieldMap = {
        name: "teamName",
        officialName: "officialName",
        slug: "teamSlug",
        abbreviation: "abbreviation",
        teamCountryCode: "countryCode",
        stagePosition: "stagePos"
    };

    Object.values(fieldMap).forEach(id => {
        const el = document.getElementById(id + "Error");
        if (el) el.innerText = "";
    });

    Object.keys(errors).forEach(field => {
        const mappedId = fieldMap[field];
        if (mappedId) {
            const el = document.getElementById(mappedId + "Error");
            if (el) {

                el.innerText = errors[field].join(", ");
            }
        }
    });
}

async function prepareEditTeam(id) {
    try {
        const team = await apiClient.get(`/teams/${id}`);
        const form = document.getElementById('teamRegistrationForm');

        document.getElementById('teamName').value = team.name;
        document.getElementById('officialName').value = team.officialName;
        document.getElementById('teamSlug').value = team.slug;
        document.getElementById('abbreviation').value = team.abbreviation;
        document.getElementById('countryCode').value = team.teamCountryCode;
        document.getElementById('stagePos').value = team.stagePosition;

        form.dataset.editId = id;
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.textContent = "Update Team in Database";
        submitBtn.classList.replace('btn-primary', 'btn-warning');

        form.scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        alert("Could not fetch team details");
    }
}

async function deleteTeam(id) {
    if (confirm('Are you sure you want to delete this team?')) {
        try {
            await apiClient.delete(`/teams/${id}`);
            loadTeams();
        } catch (error) {
            alert("Delete failed");
        }
    }
}

function resetTeamForm(form) {
    form.reset();
    delete form.dataset.editId;
    const submitBtn = form.querySelector('button[type="submit"]');
    submitBtn.textContent = "Save Team to Database";
    submitBtn.classList.replace('btn-warning', 'btn-primary');
}