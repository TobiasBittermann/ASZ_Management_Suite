import {useEffect, useState} from "react";
import MemberAddEdit from "../components/member/MemberAddEdit.jsx";
import {FiEdit3, FiPlusCircle, FiSend, FiTrash2} from "react-icons/fi";
import {Tooltip} from "react-tooltip";
import {Member} from "../types/Member";

function MembersPage() {
    const [members, setMembers] = useState<Member[]>([]);
    const [selectedMember, setSelectedMember] = useState<Member|null>(null)
    const [isModalOpen, setIsModalOpen] = useState(false)

    useEffect(() => {
        loadMembers();
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

    async function handleInviteMember(member: Member){
        const response = await fetch(`/members/${member.id}/invite`, {
            method: "POST"
        });
        if(!response.ok){
            alert("Einladung konnte nicht erstellt werden");
            return;
        }

        const {link} = await response.json();
        await navigator.clipboard.writeText(link);
        alert(`Einladungslink für ${member.firstName} ${member.lastName} wurde kopiert (24h gültig).`)
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
                        <th className={"px-6 py-3"}>Id</th>
                        <th className={"px-6 py-3"}>Vorname</th>
                        <th className={"px-6 py-3"}>Nachname</th>
                        <th className={"px-6 py-3"}>E-Mail</th>
                        <th className={"px-6 py-3"}>Kontostand</th>
                        <th className={"px-6 py-3"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {members.map((member) => (
                        <tr key={member.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{member.id}</td>
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
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                data-tooltip-id={"invite-tip"}
                                data-tooltip-content={"Einladungslink erstellen"}
                                onClick={() => handleInviteMember(member)}>
                                    <FiSend/>
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <Tooltip id={"edit-tip"}/>
                <Tooltip id={"delete-tip"}/>
                <Tooltip id={"invite-tip"}/>
            </div>
        </div>
    );
}

export default MembersPage;