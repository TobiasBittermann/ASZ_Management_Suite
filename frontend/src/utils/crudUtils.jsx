export async function saveEntity(entity, endpoint, reloadFunction, setState) {
    const isEditMode = entity.id && entity.id > 0;

    const url = isEditMode
        ? `${endpoint}/${entity.id}`
        : endpoint;

    const method = isEditMode ? "PUT" : "POST";

    const response = await fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(entity)
    });

    if (!response.ok) {
        throw new Error("Entity could not be saved!");
    }

    await reloadFunction(setState);
}

export async function deleteEntity(id, endpoint, reloadFunction, setState) {
    const response = await fetch(`${endpoint}/${id}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        throw new Error("Entity could not be deleted!");
    }

    await reloadFunction(setState);
}