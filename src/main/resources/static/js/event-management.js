document.addEventListener('DOMContentLoaded', () => {
    loadAllDropdowns();
    fetchEvents();

    const eventForm = document.getElementById('eventForm');
    if (eventForm) {
        eventForm.addEventListener('submit', handleEventSubmit);
    }
});

async function applyEventFilters() {
    let sport = document.getElementById('filterSport').value;
    const date = document.getElementById('filterDate').value;

    const resultsSection = document.getElementById('filterResultsSection');
    const resultsTable = document.getElementById('filterResultsTable');
    const resultsCount = document.getElementById('resultsCount');

    let url = "";

    if (date) {
        url = `/rest/api/events/by-date/${date}`;
    } else if (sport) {
        const sportParam = sport.trim().toUpperCase();
        url = `/rest/api/events/by-sport/${sportParam}`;
    } else {
        url = `/rest/api/events?size=100`;
    }

    console.log("Requesting Java Path:", url);

    try {
        const response = await fetch(url);

        if (!response.ok) {
            console.error("Server Error Status:", response.status);
            throw new Error(`HTTP Error: ${response.status}`);
        }

        const result = await response.json();
        console.log("API RESULT:", result);

        const events = Array.isArray(result) ? result : (result.data || result.content || []);

        if (resultsSection) {
            resultsSection.classList.remove('d-none');
            resultsSection.style.display = 'block';
        }

        if (resultsCount) {
            resultsCount.innerText = `${events.length} Found`;
        }

        if (resultsTable) {
            resultsTable.innerHTML = '';

            if (events.length === 0) {
                resultsTable.innerHTML = `<tr><td colspan="4" class="text-center py-4 text-muted">No matches found in database.</td></tr>`;
            } else {
                events.forEach(event => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td class="ps-3">
                            <span style="font-weight: 600;">${event.homeTeam?.name || 'N/A'} vs ${event.awayTeam?.name || 'N/A'}</span>
                            <small class="text-muted d-block">${event.originCompetitionName || ''}</small>
                        </td>
                        <td><div class="text-primary"><i class="fa-regular fa-calendar me-1"></i>${event.dateVenue || ''}</div></td>
                        <td><span class="badge bg-info text-dark">${event.sportName || sport || 'SPORT'}</span></td>
                        <td class="text-end pe-3 text-muted small"><i class="fa-solid fa-location-dot me-1 text-danger"></i>${event.stadium || 'N/A'}</td>
                    `;
                    resultsTable.appendChild(row);
                });
            }
        }

    } catch (error) {
        console.error("Filter failed:", error);
        if (resultsTable) {
            resultsTable.innerHTML = `<tr><td colspan="4" class="text-center py-4 text-danger">Error connecting to server.</td></tr>`;
        }
    }
}

async function loadAllDropdowns() {
    const dropdownConfigs = [
        { id: 'eventSportId', url: '/rest/api/sports', map: s => ({ val: s.id, text: s.name }) },
        { id: 'eventCompetitionId', url: '/rest/api/competitions', map: c => ({ val: c.id, text: c.name }) },
        { id: 'eventStageId', url: '/rest/api/stages', map: st => ({ val: st.id, text: st.name }) },
        { id: 'eventVenueId', url: '/rest/api/venues', map: v => ({ val: v.id, text: `${v.name} (${v.city})` }) },
        { id: 'eventHomeTeam', url: '/rest/api/teams', map: t => ({ val: t.slug, text: t.name }) }
    ];

    for (const config of dropdownConfigs) {
        try {
            const response = await fetch(config.url);
            const rawData = await response.json();
            const list = Array.isArray(rawData) ? rawData : (rawData.data || []);
            fillSelect(config.id, list, config.map);

            if (config.id === 'eventHomeTeam') {
                fillSelect('eventAwayTeam', list, config.map);
            }

        } catch (e) {
            console.error(e);
        }
    }
}

function fillSelect(elementId, data, mapper) {
    const select = document.getElementById(elementId);
    if (!select) return;

    const firstOption = select.options[0];
    select.innerHTML = '';
    if (firstOption) select.appendChild(firstOption);

    data.forEach(item => {
        const info = mapper(item);
        const opt = document.createElement('option');
        opt.value = info.val;
        opt.textContent = info.text;
        select.appendChild(opt);
    });
}

async function fetchEvents() {
    try {
        const response = await fetch('/rest/api/events?size=50');
        const result = await response.json();
        renderEventTable(result.data || result || []);
    } catch (e) {
        console.error(e);
    }
}

function renderEventTable(events) {
    const tableBody = document.getElementById('eventListTable');
    if (!tableBody) return;

    tableBody.innerHTML = '';

    events.forEach(event => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>
                <span style="font-weight: 600;">
                    ${event.homeTeam?.name} vs ${event.awayTeam?.name}
                </span>
            </td>
            <td><div class="text-info">${event.dateVenue}</div></td>
            <td>${event.stadium || 'Venue'}</td>
            <td class="text-end">

                <button class="btn btn-sm btn-outline-info me-1"
                        onclick="viewDetails('${event.id}')">
                    <i class="fa-solid fa-eye"></i>
                </button>

                <button class="btn btn-sm btn-outline-warning me-1"
                        onclick="editEvent('${event.id}')">
                    <i class="fa-solid fa-pen"></i>
                </button>

                <button class="btn btn-sm btn-outline-danger"
                        onclick="deleteEvent('${event.id}')">
                    <i class="fa-solid fa-trash"></i>
                </button>

            </td>
        `;

        tableBody.appendChild(row);
    });
}

async function viewDetails(id) {
    try {
        const res = await fetch(`/rest/api/events/${id}`);
        const event = await res.json();

        const detailsContainer = document.getElementById('eventDetails');

        detailsContainer.innerHTML = `
            <div class="container-fluid">

                <div class="card mb-3">
                    <div class="card-body">
                        <h4 class="text-primary">
                            ${event.homeTeam?.name || 'N/A'} vs ${event.awayTeam?.name || 'N/A'}
                        </h4>

                        <p><strong>Sport:</strong> ${event.sportName || 'N/A'}</p>
                        <p><strong>Competition:</strong> ${event.originCompetitionName || 'N/A'}</p>
                        <p><strong>Season:</strong> ${event.season || 'N/A'}</p>
                        <p><strong>Status:</strong> ${event.status || 'N/A'}</p>
                        <p><strong>Stage:</strong> ${event.stage?.name || event.stage?.id || 'N/A'}</p>
                    </div>
                </div>

                <div class="card mb-3">
                    <div class="card-body">
                        <h5>Match Info</h5>
                        <p><strong>Date:</strong> ${event.dateVenue || 'N/A'}</p>
                        <p><strong>Time:</strong> ${event.timeVenueUTC || 'N/A'}</p>
                        <p><strong>Stadium:</strong> ${event.stadium || 'N/A'}</p>
                    </div>
                </div>

                ${event.result ? `
                    <div class="card mb-3 border-success">
                        <div class="card-body">
                            <h5 class="text-success">Result</h5>

                            <p><strong>Score:</strong>
                                ${event.result.homeGoals} - ${event.result.awayGoals}
                            </p>

                            <p><strong>Winner:</strong> ${event.result.winner || 'N/A'}</p>

                            <hr>

                            <p><strong>Goals:</strong></p>
                            <ul>
                                ${(event.result.goals || []).map(g => `<li>${g}</li>`).join('')}
                            </ul>

                            <p><strong>Yellow Cards:</strong></p>
                            <ul>
                                ${(event.result.yellowCards || []).map(g => `<li>${g}</li>`).join('')}
                            </ul>

                            <p><strong>Red Cards:</strong></p>
                            <ul>
                                ${(event.result.directRedCards || []).map(g => `<li>${g}</li>`).join('')}
                            </ul>
                        </div>
                    </div>
                ` : `
                    <div class="card mb-3">
                        <div class="card-body text-muted">
                            <p>No result available yet.</p>
                        </div>
                    </div>
                `}

            </div>
        `;

    } catch (error) {
        console.error(error);
        alert("Error loading event details");
    }
}

async function handleEventSubmit(e) {
    e.preventDefault();

    const dateTimeInput = document.getElementById('eventDate').value;
    if (!dateTimeInput) return;

    const [date, time] = dateTimeInput.split('T');

    const eventData = {
        competitionId: document.getElementById('eventCompetitionId').value,
        stageId: document.getElementById('eventStageId').value,
        date: date,
        timeUtc: time.length === 5 ? time + ":00" : time,
        status: "scheduled",
        venueId: parseInt(document.getElementById('eventVenueId').value),
        homeTeamSlug: document.getElementById('eventHomeTeam').value,
        awayTeamSlug: document.getElementById('eventAwayTeam').value
    };

    const editId = e.target.dataset.editId;
    const url = editId ? `/rest/api/events/${editId}` : '/rest/api/events';

    try {
        const res = await fetch(url, {
            method: editId ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(eventData)
        });

        if (res.ok) {
            fetchEvents();
            resetForm('eventForm');
        }

    } catch (e) {
        console.error(e);
    }
}

async function editEvent(id) {
    try {
        const res = await fetch(`/rest/api/events/${id}`);
        const event = await res.json();

        const form = document.getElementById('eventForm');
        form.dataset.editId = id;

        document.getElementById('eventCompetitionId').value = event.originCompetitionId;
        document.getElementById('eventHomeTeam').value = event.homeTeam.slug;
        document.getElementById('eventAwayTeam').value = event.awayTeam.slug;
        document.getElementById('eventVenueId').value = event.venueId;

        form.querySelector('button[type="submit"]').textContent = "Update Event";

    } catch (e) {
        console.error(e);
    }
}

async function deleteEvent(id) {
    if (!confirm("Delete this event?")) return;

    try {
        const res = await fetch(`/rest/api/events/${id}`, { method: 'DELETE' });
        if (res.ok) fetchEvents();

    } catch (e) {
        console.error(e);
    }
}

function resetForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.reset();
    delete form.dataset.editId;

    const btn = form.querySelector('button[type="submit"]');
    if (btn) btn.textContent = "Save Event";
}