using DataFrames, Plots, CSV

cumulativeIE = false

SIRframe = DataFrame(CSV.File("./DataSEIR.csv"))

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

plotName = "Model_0.png"
save = true
if save
    savefig(SIRplot, "./savedPlots/$(plotName)")
end
