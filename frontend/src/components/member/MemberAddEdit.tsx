import {useEffect, useState, type FormEvent} from "react";
import type {Member} from "../../types/Member.ts";
import type {StatusOption} from "../../types/StatusOption.ts";

type MemberAddEditProps = {
    member: Member | null;
    onClose: () => void;
    onSave: (member: Member) => Promise<void>;
}

function MemberAddEdit({member, onClose, onSave}: MemberAddEditProps) {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [status, setStatus] = useState("");
    const [statusOptions, setStatusOptions] = useState<StatusOption[]>([]);
    const [balance, setBalance] = useState("");
    const [showInBeerlist, setShowInBeerlist] = useState(false)

    useEffect(() => {
        if (member) {
            // eslint-disable-next-line react-hooks/set-state-in-effect
            setFirstName(member.firstName)
            setLastName(member.lastName)
            setEmail(member.email)
            setStatus(member.status ?? "")
            setBalance(member.balance?.toString() ?? "")
            setShowInBeerlist(member.showInBeerlist)
        } else {
            setFirstName("")
            setLastName("")
            setEmail("")
            setStatus("")
            setBalance("")
            setShowInBeerlist(false)
        }
    }, [member]);

    useEffect(() => {
        fetch(`/enums/status`)
            .then(res => res.json())
            .then(setStatusOptions)
    }, [])

    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        const savedMember: Member = {
            id: member ? member.id : 0,
            firstName,
            lastName,
            email,
            status: status || null,
            balance: Number(balance),
            showInBeerlist: showInBeerlist
        }

        await onSave(savedMember)
        onClose();
    }

    return (
        <div className={"fixed inset-0 bg-black/50 flex items-center justify-center z-50"}>
            <div className={"bg-white rounded-2xl shadow-xl p-8 w-full max-w-md"}>
                <h2 className={"text-2xl font-bold text-gray-800 mb-6"}>
                    {member ? "Edit Member" : "Add Member"}
                </h2>

                <form
                    className={"grid grid-cols-[auto_1fr] items-center gab-x-4 gap-y-4"}
                    onSubmit={handleSubmit}>
                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Vorname:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"text"}
                        value={firstName}
                        onChange={(event) => setFirstName(event.target.value)}
                    />
                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Nachname:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"text"}
                        value={lastName}
                        onChange={(event) => setLastName(event.target.value)}
                    />
                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        E-Mail:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"text"}
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                    />
                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Status:
                    </label>
                    <select
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        value={status}
                        onChange={(event) => setStatus((event.target.value))}
                    >
                        <option value="">Bitte wählen</option>
                        {statusOptions.map(option => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Kontostand:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"number"}
                        value={balance}
                        onChange={(event) => setBalance(event.target.value)}
                    />
                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Member in Bierliste anzeigen:
                    </label>
                    <select
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        value={String(showInBeerlist)}
                        onChange={(event) => setShowInBeerlist(event.target.value === "true")}
                    >
                        <option value={"true"}>Ja</option>
                        <option value={"false"}>Nein</option>
                    </select>
                    <div className={"col-span-2 flex justify-end gab-3 mt-2"}>
                        <button
                            className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-6 py-2 m-1 transition"}
                            type={"submit"}>
                            Save
                        </button>
                        <button
                            className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-6 py-2 m-1 transition"}
                            type={"button"} onClick={onClose}>
                            Close
                        </button>
                    </div>
                </form>

            </div>
        </div>
    )
}

export default MemberAddEdit;