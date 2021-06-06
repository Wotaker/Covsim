using DataFrames, Plots, CSV

plotName = "Mdl_SparseVery_(100000, 100, (25000, 4000, 1600)).png"
fileCSVname = "DataSEIR.csv"
save = true
cumulativeIE = false

SIRframe = DataFrame(CSV.File("./savedData/$fileCSVname"))

SIRplot = plot(
    SIRframe[!, :Day], 
    SIRframe[!, :Suspectible],
    title = "SEIR Model Evolution",
    xlabel = "Day",
    ylabel = "Cases",
    label = "Suspectible",
    color = 1
)
if cumulativeIE
    plot!(
        SIRplot,
        SIRframe[!, :Exposed] + SIRframe[!, :Infectious],
        label = "Infectious",
        color = :red
    )
else
    plot!(
        SIRplot,
        SIRframe[!, :Exposed],
        label = "Exposed",
        color = 2
    )
    plot!(
        SIRplot,
        SIRframe[!, :Infectious],
        label = "Infectious",
        color = :red
    )
end
plot!(
    SIRplot,
    SIRframe[!, :Recovered],
    label = "Recovered",
    color = :green
)
plot!(
    SIRplot,
    SIRframe[!, :Dead],
    label = "Dead",
    color = :Black
)

display(SIRplot)

if save
    savefig(SIRplot, "./savedPlots/$(plotName)")
end
