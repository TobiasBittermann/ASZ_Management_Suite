export async function loadAccountTypes(setAccountTypes) {
    const response = await fetch("/account-types");

    if (!response.ok) {
        throw new Error("Loading account types failed");
    }

    const data = await response.json();
    setAccountTypes(data);
}

export async function loadMembers(setMembers) {
    const response = await fetch("/members")

    if (!response.ok) {
        throw new Error("Loading members failed")
    }

    const data = await response.json()
    setMembers(data);
}

export async function loadBwDeposits(setBwDeposits) {
    const response = await fetch("/bwdeposits");

    if (!response.ok) {
        throw new Error("Loading deposits failed");
    }

    const data = await response.json();
    setBwDeposits(data);
}

export async function loadDrinks(setDrinks) {
    const response = await fetch("/drinks");

    if (!response.ok) {
        throw new Error("Loading drinks failed");
    }

    const data = await response.json();
    setDrinks(data);
}

export async function loadAccountBookings(setAccountBookings) {
    const response = await fetch("/bwaccountbookings");

    if (!response.ok) {
        throw new Error("Loading bookings failed")
    }

    const data = await response.json();
    setAccountBookings(data);
}

export async function loadVendors(setVendors) {
    const response = await fetch("/vendors")

    if (!response.ok) {
        throw new Error("Loading vendors dailed")
    }

    const data = await response.json();
    setVendors(data);
}

export async function loadBwBookings(setBwBookings) {
    const response = await fetch("/bwbookings");

    if (!response.ok) {
        throw new Error("Loading bookings failed");
    }

    const data = await response.json();
    setBwBookings(data);
}

export async function loadSnapshots(setSnapshots) {
    const response = await fetch("/bwsnapshots");

    if (!response.ok) {
        throw new Error("Loading snapshots failed");
    }

    const data = await response.json();
    setSnapshots(data)
}

export async function loadInventoryEntries(setInventoryEntries){
    const response = await fetch("/inventoryentries")

    if (!response.ok){
        throw new Error("Loading inventory entries failed")
    }


    const data = await response.json();
    setInventoryEntries(data);
}

export async function loadInventories(setInventories){
    const response = await fetch ("/inventories")

    if(!response.ok){
        throw new Error("Loading inventories failed")
    }

    const data = await response.json();
    setInventories(data);
}