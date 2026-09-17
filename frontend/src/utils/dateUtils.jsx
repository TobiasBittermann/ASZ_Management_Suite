export function getCurrentLocalDateTime() {
    const now = new Date();
    const local = new Date(now.getTime() - now.getTimezoneOffset() * 60000);
    return local.toISOString().slice(0, 16);
}

export function getCurrentLocalDateDaysAgo(daysAgo){
    const now = new Date();
    now.setDate(now.getDate() - daysAgo);
    const local = new Date(now.getTime() - now.getTimezoneOffset() * 60000);
    return local.toISOString().slice(0, 10);
}