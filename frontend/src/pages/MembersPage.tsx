import {useEffect, useState} from "react";
import MemberAddEdit from "../components/member/MemberAddEdit.tsx";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi";
import {Tooltip} from "react-tooltip";
import type {Member} from "../types/Member.ts";
import type {StatusOption} from "../types/StatusOption.ts";

type SortKey = "id" | "status" | "firstName" | "lastName" | "email" | "balance";

function MembersPage() {
    const [members, setMembers] = useState<Member[]>([]);
    const [selectedMember, setSelectedMember] = useState<Member | null>(null)
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [statusOptions, setStatusOptions] = useState<StatusOption[]>([])
    const [sortKey, setSortKey] = useState<SortKey>("lastName")
    const [sortAsc, setSortAsc] = useState(true)
    const [search, setSearch] = useState("")

    useEffect(() => {
        loadMembers();
        fetch("/enums/status")
            .then(res => res.json())
            .then(setStatusOptions)
    }, [])

    async function loadMembers() {
        const response = await fetch("/members");

        if (!response.ok) {
            throw new Error("Loading members failed")
        }

        const data = await response.json();
        setMembers(data);
    }

    async function handleSaveMember(member: Member) {
        const isEditMode = member.id && member.id > 0;
        const url = isEditMode
            ? `/members/${member.id}`
            : "/members";

        const method = isEditMode ? "PUT" : "POST";

        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(member)
        });

        if (!response.ok) {
            throw new Error("Member could not be saved!")
        }

        await loadMembers();
    }

    function handleEditClick(member: Member) {
        setSelectedMember(member);
        setIsModalOpen(true);
    }

    function handleAddClick() {
        setSelectedMember(null);
        setIsModalOpen(true);
    }

    async function handleDeleteMember(id: number) {
        const response = await fetch(`/members/${id}`, {
            method: "DELETE"
        });
        if (!response.ok) {
            throw new Error("Member could not be deleted!")
        }

        await loadMembers();
    }

    function getStatusLabel(value: string | null) {
        return statusOptions.find(option => option.value === value)?.label ?? value ?? "";
    }

    function handleSort(key: SortKey) {
        if (key === sortKey) {
            setSortAsc(!sortAsc);
        } else {
            setSortKey(key);
            setSortAsc(true);
        }
    }

    const term = search.trim().toLowerCase();
    const filteredMembers = members.filter(member => [member.firstName, member.lastName, member.email].some(value => value?.toLowerCase().includes(term)));

    const sortedMembers = filteredMembers.sort((a, b) => {
        const valueA = sortKey === "status" ? getStatusLabel(a.status) : a[sortKey];
        const valueB = sortKey === "status" ? getStatusLabel(b.status) : b[sortKey];

        if (valueA == null) return 1;
        if (valueB == null) return -1;

        const result = typeof valueA === "number" && typeof valueB === "number" ? valueA - valueB : String(valueA).localeCompare(String(valueB), "de");

        return sortAsc ? result : -result
    });

    function sortArrow(key: SortKey) {
        return sortKey === key ? (sortAsc ? " ▲" : " ▼") : "";
    }


    return (
        <div>
            <h1 className={"text-3xl font-bold text-gray-800 text-center"}>
                Mitgliederverzeichnis
            </h1>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Add a new member"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>
            <Tooltip id={"add-tip"}/>

            <input
                className={"border border-gray-300 rounded-lg px-3 py-2 m-3 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                type={"text"}
                placeholder={"Mitglied suchen"}
                value={search}
                onChange={(event) => setSearch(event.target.value)}
            />

            {isModalOpen && (
                <MemberAddEdit
                    member={selectedMember}
                    onClose={() => setIsModalOpen(false)}
                    onSave={handleSaveMember}/>

            )}

            <div className={"overflow-x-auto rounded-xl shadow"}>

                <table className={"min-w-full bg-white text-sm text-left"}>
                    <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                    <tr>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("id")}>Id{sortArrow("id")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("status")}>Status{sortArrow("status")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("firstName")}>Vorname{sortArrow("firstName")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("lastName")}>Nachname{sortArrow("lastName")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("email")}>E-Mail{sortArrow("email")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("balance")}>Kontostand{sortArrow("balance")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {sortedMembers.map((member) => (
                        <tr key={member.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{member.id}</td>
                            <td className={"px-6 py-3"}>{getStatusLabel(member.status)}</td>
                            <td className={"px-6 py-3"}>{member.firstName}</td>
                            <td className={"px-6 py-3"}>{member.lastName}</td>
                            <td className={"px-6 py-3"}>{member.email}</td>
                            <td className={"px-6 py-3"}>{member.balance}</td>
                            <td>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"edit-tip"}
                                    data-tooltip-content={"Edit a member entry"}
                                    onClick={() => handleEditClick(member)}>
                                    <FiEdit3/>
                                </button>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"delete-tip"}
                                    data-tooltip-content={"Delete a member entry"}
                                    onClick={() => handleDeleteMember(member.id)}>
                                    <FiTrash2/>
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <Tooltip id={"edit-tip"}/>
                <Tooltip id={"delete-tip"}/>
            </div>
        </div>
    );
}

export default MembersPage;