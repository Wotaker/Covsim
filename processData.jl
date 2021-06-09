using DataFrames, Plots, CSV

plotName = "Mdl_X.png"
fileCSVname = "DataSEIR.csv"
save = false
cumulativeIE = false
allData = true

SIRframe = DataFrame(CSV.File("./savedData/$fileCSVname"))


SIRplot = plot(
    SIRframe[!, :Day], 
    SIRframe[!, :Dead],
    title = "SEIR Model Evolution",
    xlabel = "Day",
    ylabel = "Cases",
    label = "Dead",
    color = :Black
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
if allData
    plot!(
        SIRplot,
        SIRframe[!, :Suspectible],
        label = "Suspectible",
        color = 1
    )
    plot!(
        SIRplot,
        SIRframe[!, :Recovered],
        label = "Recovered",
        color = :green
    )
end

display(SIRplot)

if save
    savefig(SIRplot, "./savedPlots/$(plotName)")
end
